FROM nginx:1.27.4-alpine AS builder
ARG NGINX_VERSION=1.27.4

RUN apk add --no-cache --virtual .build-deps \
      gcc make libtool automake autoconf pcre-dev zlib-dev linux-headers openssl-dev \
      git g++ cmake wget

RUN git clone --recurse-submodules -j8 https://github.com/google/ngx_brotli.git /usr/src/ngx_brotli

# ngx_brotli's bundled deps/brotli submodule must be compiled to static libs
# (libbrotlienc.a, libbrotlicommon.a) before nginx's build links against them.
RUN mkdir -p /usr/src/ngx_brotli/deps/brotli/out \
    && cd /usr/src/ngx_brotli/deps/brotli/out \
    && cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=OFF -DBROTLI_DISABLE_TESTS=ON \
             -DCMAKE_C_FLAGS="-fPIC" -DCMAKE_INSTALL_PREFIX=./installed .. \
    && cmake --build . --config Release --target brotlienc

RUN wget "http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz" -O /tmp/nginx.tar.gz \
    && mkdir -p /usr/src && tar -zxC /usr/src -f /tmp/nginx.tar.gz

RUN CONFARGS=$(nginx -V 2>&1 | sed -n 's/^configure arguments://p') \
    && cd /usr/src/nginx-${NGINX_VERSION} \
    && eval "./configure $CONFARGS --add-dynamic-module=/usr/src/ngx_brotli" \
    && make modules

FROM nginx:1.27.4-alpine
ARG NGINX_VERSION=1.27.4
COPY --from=builder /usr/src/nginx-${NGINX_VERSION}/objs/ngx_http_brotli_filter_module.so /etc/nginx/modules/
COPY --from=builder /usr/src/nginx-${NGINX_VERSION}/objs/ngx_http_brotli_static_module.so /etc/nginx/modules/
