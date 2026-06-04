#!/bin/bash
# ============================================================
# ✨ Starluck · 一键部署脚本
# ============================================================
# 用法：
#   本机运行（已有项目目录）：   ./deploy.sh
#   仅构建镜像不启动：           ./deploy.sh build
#   查看日志：                   ./deploy.sh logs
#
# 前提：
#   - 本机已安装 Docker
#   - docker compose 可用
# ============================================================

set -e

SERVER_IP="8.140.249.127"
SSH_KEY="$HOME/.ssh/starluck.pem"
APP_DIR="/opt/starluck"

echo "=========================================="
echo " ✨ Starluck Server · 部署脚本"
echo "=========================================="
echo ""

# === 第一步：安装 Docker（仅在服务器首次部署时运行） ===
install_docker() {
    echo ">>> 检查 Docker 是否已安装..."
    ssh -i "$SSH_KEY" root@"$SERVER_IP" "docker --version" 2>/dev/null && {
        echo "✅ Docker 已安装，跳过"
        return
    }

    echo ">>> 安装 Docker..."
    ssh -i "$SSH_KEY" root@"$SERVER_IP" << 'EOF'
        # 卸载旧版本
        for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do
            apt-get remove -y $pkg 2>/dev/null || true
        done

        # 安装 Docker
        apt-get update -qq
        apt-get install -y -qq ca-certificates curl
        install -m 0755 -d /etc/apt/keyrings
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
        chmod a+r /etc/apt/keyrings/docker.asc

        echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
            tee /etc/apt/sources.list.d/docker.list > /dev/null

        apt-get update -qq
        apt-get install -y -qq docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

        # 设置 Docker 开机自启
        systemctl enable docker
        systemctl start docker

        # 添加常用命令别名
        echo 'alias dc="docker compose"' >> ~/.bashrc
EOF
    echo "✅ Docker 安装完成"
}

# === 第二步：上传项目到服务器 ===
upload() {
    echo ""
    echo ">>> 上传项目文件到服务器..."

    ssh -i "$SSH_KEY" root@"$SERVER_IP" "mkdir -p $APP_DIR"

    # 传 docker-compose.yml
    scp -i "$SSH_KEY" docker-compose.yml root@"$SERVER_IP":"$APP_DIR"/docker-compose.yml

    # 传 starluck-server（排除 target 目录以减少上传量）
    rsync -avz --delete \
        --exclude 'target/' \
        --exclude '.git/' \
        --exclude 'node_modules/' \
        -e "ssh -i $SSH_KEY" \
        starluck-server/ root@"$SERVER_IP":"$APP_DIR"/starluck-server/

    echo "✅ 项目文件上传完成"
}

# === 第三步：构建镜像 ===
build() {
    echo ""
    echo ">>> 在服务器上构建 Docker 镜像（约 3-5 分钟）..."
    ssh -i "$SSH_KEY" root@"$SERVER_IP" << EOF
        cd "$APP_DIR"
        docker compose build app
EOF
    echo "✅ 镜像构建完成"
}

# === 第四步：启动服务 ===
start() {
    echo ""
    echo ">>> 启动所有服务..."
    ssh -i "$SSH_KEY" root@"$SERVER_IP" << EOF
        cd "$APP_DIR"
        docker compose up -d
        echo ""
        echo ">>> 等待服务就绪..."
        sleep 8
        docker compose ps
        echo ""
        echo ">>> 检查 app 健康状态..."
        curl -s -o /dev/null -w "HTTP %{http_code}" http://localhost:8080/actuator/health || echo " （actuator 未启用，检查应用日志）"
        echo ""
EOF
    echo "✅ 服务启动完成"
}

# === 查看日志 ===
logs() {
    ssh -i "$SSH_KEY" root@"$SERVER_IP" "cd $APP_DIR && docker compose logs -f --tail=100 app"
}

# === 停止服务 ===
stop() {
    ssh -i "$SSH_KEY" root@"$SERVER_IP" "cd $APP_DIR && docker compose down"
    echo "✅ 已停止"
}

# === 重启 ===
restart() {
    ssh -i "$SSH_KEY" root@"$SERVER_IP" "cd $APP_DIR && docker compose restart app"
    echo "✅ 已重启"
}

# === 状态 ===
status() {
    ssh -i "$SSH_KEY" root@"$SERVER_IP" "cd $APP_DIR && docker compose ps && echo '' && echo '=== 磁盘 ===' && df -h / | tail -1 && echo '' && echo '=== 内存 ===' && free -h | head -2"
}

# === 命令行子命令 ===
case "${1:-deploy}" in
    install)
        install_docker
        ;;
    upload)
        upload
        ;;
    build)
        upload
        build
        ;;
    start)
        upload
        build
        start
        ;;
    deploy)
        install_docker
        upload
        build
        start
        ;;
    logs)
        logs
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        echo "用法: ./deploy.sh [子命令]"
        echo ""
        echo "  deploy    一键首次部署（安装Docker + 上传 + 构建 + 启动）"
        echo "  upload    只上传文件"
        echo "  build     上传 + 构建镜像"
        echo "  start     上传 + 构建 + 启动"
        echo "  logs      查看应用日志"
        echo "  stop      停止所有服务"
        echo "  restart   重启应用"
        echo "  status    查看服务状态"
        echo "  install   只安装 Docker"
        ;;
esac

echo ""
echo "=========================================="
echo " 部署完毕 · 访问 http://$SERVER_IP:8080"
echo "=========================================="
