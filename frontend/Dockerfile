FROM node:13.11.0

RUN npm install -g @angular/cli

ARG node_env=production
ENV NODE_ENV $node_env

WORKDIR /app

ADD package.json .
ADD package-lock.json .
RUN npm install

ADD . .

ENTRYPOINT ["ng", "serve", "--proxy-config", "proxy-k8s.conf.json"]
