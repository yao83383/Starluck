# Starluck AI 会话备份

## 从另一台电脑恢复

1. 把 `opencode.db` 放到这个文件夹（单独传输，太大不进 git）
2. 右键 `restore.ps1` → 用 PowerShell 运行
3. 重开 opencode 即恢复全部对话

## 传输 opencode.db 的方法

本机打包：`Compress-Archive opencode.db opencode-db.zip`
（约 30MB，可微信/网盘发送）
