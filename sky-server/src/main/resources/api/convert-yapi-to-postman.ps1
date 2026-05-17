param(
    [string]$InputDir = $PSScriptRoot,
    [string]$BaseUrl = "http://localhost:8080"
)

function ConvertTo-SampleValue {
    param($Schema, [int]$Depth = 0)

    if ($null -eq $Schema -or $Depth -gt 8) {
        return $null
    }

    $type = $Schema.type
    if ($type -is [array]) {
        $type = $type | Where-Object { $_ -ne "null" } | Select-Object -First 1
    }

    switch ($type) {
        "object" {
            $obj = [ordered]@{}
            if ($Schema.properties) {
                foreach ($prop in $Schema.properties.PSObject.Properties) {
                    $obj[$prop.Name] = ConvertTo-SampleValue $prop.Value ($Depth + 1)
                }
            }
            return $obj
        }
        "array" {
            return @((ConvertTo-SampleValue $Schema.items ($Depth + 1)))
        }
        "integer" {
            return 0
        }
        "number" {
            return 0
        }
        "boolean" {
            return $true
        }
        default {
            return ""
        }
    }
}

function ConvertFrom-JsonSchemaText {
    param([string]$SchemaText)

    if ([string]::IsNullOrWhiteSpace($SchemaText)) {
        return $null
    }

    try {
        $schema = $SchemaText | ConvertFrom-Json
        return ConvertTo-SampleValue $schema
    }
    catch {
        return $SchemaText
    }
}

function Get-TextOrFallback {
    param($Primary, $Fallback)

    if (-not [string]::IsNullOrWhiteSpace([string]$Primary)) {
        return [string]$Primary
    }

    return [string]$Fallback
}

function ConvertTo-PostmanRequest {
    param($Api)

    $rawPath = [string]$Api.path
    $postmanPath = $rawPath -replace "\{([^}]+)\}", ":`$1"
    $pathParts = $postmanPath.Trim("/") -split "/" | Where-Object { $_ -ne "" }

    $query = @()
    foreach ($q in @($Api.req_query)) {
        if ([string]::IsNullOrWhiteSpace($q.name)) {
            continue
        }

        $query += [ordered]@{
            key = [string]$q.name;
            value = [string]$q.example;
            description = [string]$q.desc;
            disabled = ($q.required -ne "1");
        }
    }

    $variables = @()
    foreach ($p in @($Api.req_params)) {
        if ([string]::IsNullOrWhiteSpace($p.name)) {
            continue
        }

        $variables += [ordered]@{
            key = [string]$p.name;
            value = [string]$p.example;
            description = [string]$p.desc;
        }
    }

    $headers = @()
    foreach ($h in @($Api.req_headers)) {
        if ([string]::IsNullOrWhiteSpace($h.name)) {
            continue
        }

        $headers += [ordered]@{
            key = [string]$h.name;
            value = [string]$h.value;
            description = [string]$h.desc;
        }
    }

    $description = Get-TextOrFallback $Api.markdown $Api.desc

    $request = [ordered]@{
        method = ([string]$Api.method).ToUpperInvariant();
        header = $headers;
        url = [ordered]@{
            raw = "{{baseUrl}}$postmanPath";
            host = @("{{baseUrl}}");
            path = @($pathParts);
            query = $query;
            variable = $variables;
        };
        description = $description;
    }

    if ($Api.req_body_type -eq "json") {
        $sample = ConvertFrom-JsonSchemaText ([string]$Api.req_body_other)
        $request.body = [ordered]@{
            mode = "raw";
            raw = ($sample | ConvertTo-Json -Depth 20);
            options = [ordered]@{
                raw = [ordered]@{
                    language = "json";
                };
            };
        }
    }
    elseif ($Api.req_body_type -eq "form") {
        $formdata = @()
        foreach ($field in @($Api.req_body_form)) {
            if ([string]::IsNullOrWhiteSpace($field.name)) {
                continue
            }

            $fieldType = "text"
            $fieldSrc = $null
            $fieldValue = [string]$field.example
            if ($field.type -eq "file") {
                $fieldType = "file"
                $fieldSrc = ""
                $fieldValue = $null
            }

            $formdata += [ordered]@{
                key = [string]$field.name;
                type = $fieldType;
                src = $fieldSrc;
                value = $fieldValue;
                description = [string]$field.desc;
            }
        }

        $request.body = [ordered]@{
            mode = "formdata";
            formdata = $formdata;
        }
    }

    return [ordered]@{
        name = [string]$Api.title;
        request = $request;
        response = @();
    }
}

function ConvertTo-PostmanCollection {
    param($YApi, [string]$CollectionName, [string]$BaseUrl)

    $groups = [ordered]@{}
    foreach ($api in @($YApi.list)) {
        $tag = "未分组"
        if ($api.tag -and $api.tag.Count -gt 0 -and -not [string]::IsNullOrWhiteSpace($api.tag[0])) {
            $tag = [string]$api.tag[0]
        }

        if (-not $groups.Contains($tag)) {
            $groups[$tag] = @()
        }

        $groups[$tag] += ConvertTo-PostmanRequest $api
    }

    $items = @()
    foreach ($group in $groups.GetEnumerator()) {
        $items += [ordered]@{
            name = $group.Key;
            item = @($group.Value);
        }
    }

    return [ordered]@{
        info = [ordered]@{
            name = $CollectionName;
            schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";
        };
        item = $items;
        variable = @(
            [ordered]@{
                key = "baseUrl";
                value = $BaseUrl;
                type = "string";
            }
        );
    }
}

$inputFiles = Get-ChildItem -LiteralPath $InputDir -Filter "*.json" |
    Where-Object { $_.Name -notlike "*postman*" }

$collections = @()
foreach ($file in $inputFiles) {
    $yapi = Get-Content -LiteralPath $file.FullName -Raw | ConvertFrom-Json
    $collectionName = [IO.Path]::GetFileNameWithoutExtension($file.Name)
    $collection = ConvertTo-PostmanCollection $yapi $collectionName $BaseUrl
    $collections += $collection

    $outPath = Join-Path $InputDir "$collectionName.postman_collection.json"
    $collection | ConvertTo-Json -Depth 50 | Set-Content -LiteralPath $outPath -Encoding UTF8
    Write-Output "Wrote $outPath"
}

if ($collections.Count -gt 1) {
    $merged = [ordered]@{
        info = [ordered]@{
            name = "苍穹外卖-全部接口";
            schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";
        };
        item = @();
        variable = @(
            [ordered]@{
                key = "baseUrl";
                value = $BaseUrl;
                type = "string";
            }
        );
    }

    foreach ($collection in $collections) {
        $merged.item += [ordered]@{
            name = $collection.info.name;
            item = @($collection.item);
        }
    }

    $mergedOutPath = Join-Path $InputDir "苍穹外卖-全部接口.postman_collection.json"
    $merged | ConvertTo-Json -Depth 60 | Set-Content -LiteralPath $mergedOutPath -Encoding UTF8
    Write-Output "Wrote $mergedOutPath"
}
