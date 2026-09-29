FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
HEALTHCHECK --interval=20s --timeout=4s --start-period=8s --retries=4 CMD wget -qO- http://127.0.0.1/healthz || exit 1
