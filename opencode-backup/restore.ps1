# ✨ Starluck · Opencode 会话恢复脚本
# 在新电脑上右键"使用 PowerShell 运行"即可
# 或：.\restore.ps1

$ErrorActionPreference = "Stop"
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " ✨ Starluck · Opencode 会话恢复" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$BACKUP = Split-Path -Parent $PSCommandPath
$TARGET_DATA = "$env:USERPROFILE\.local\share\opencode"
$TARGET_CFG  = "$env:APPDATA\opencode"

Write-Host ">>> 恢复 opencode 对话数据库..."
if (!(Test-Path "$TARGET_DATA")) { New-Item -ItemType Directory -Path "$TARGET_DATA" -Force | Out-Null }

Copy-Item "$BACKUP\opencode.db"  "$TARGET_DATA\opencode.db" -Force
Copy-Item "$BACKUP\auth.json"    "$TARGET_DATA\auth.json" -Force

Write-Host ">>> 恢复 opencode 配置..."
if (!(Test-Path "$TARGET_CFG")) { New-Item -ItemType Directory -Path "$TARGET_CFG" -Force | Out-Null }

Copy-Item "$BACKUP\opencode.jsonc" "$TARGET_CFG\opencode.jsonc" -Force -ErrorAction SilentlyContinue
Copy-Item "$BACKUP\AGENTS.md"      "$TARGET_CFG\AGENTS.md" -Force -ErrorAction SilentlyContinue

Write-Host ">>> 恢复会话 diff..."
if (!(Test-Path "$TARGET_DATA\storage\session_diff")) {
  New-Item -ItemType Directory -Path "$TARGET_DATA\storage\session_diff" -Force | Out-Null
}

Get-ChildItem "$BACKUP\sessions\ses_*.json" | ForEach-Object {
  Copy-Item $_.FullName "$TARGET_DATA\storage\session_diff\" -Force
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host " ✅ 恢复完成！" -ForegroundColor Green
Write-Host ""
Write-Host "下次打开 opencode 时，所有对话历史将自动加载。"
Write-Host ""
Write-Host "注意：如果你已经在这台电脑上有 opencode 对话，"
Write-Host "此操作会覆盖它们。如需合并，请先备份原有数据。"
Write-Host "========================================" -ForegroundColor Green
