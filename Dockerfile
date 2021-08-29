FROM        ubuntu:latest

ENV         DEBIAN_FRONTEND noninteractive

RUN         apt-get update
RUN         apt-get install -y build-essential cmake curl pkg-config libicu-dev zlib1g-dev libcurl4-openssl-dev libssl-dev nodejs ruby-dev

RUN         mkdir /graalvm/
RUN         curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.2.0/graalvm-ce-java11-linux-amd64-21.2.0.tar.gz | tar -xz --strip-components=1 -C /graalvm/
ENV         PATH $PATH:/graalvm/bin/
ENV         JAVA_HOME /graalvm/

RUN         gu install ruby
RUN         $JAVA_HOME/languages/ruby/lib/truffle/post_install_hook.sh
RUN         gem install github-linguist

RUN         mkdir /root/bin/
ENV         PATH $PATH:/root/bin/
RUN         curl -Ls https://git.io/sbt > /root/bin/sbt && chmod 0755 /root/bin/sbt

COPY        ./project/build.properties ./project/plugins.sbt /cache/project/
COPY        ./build.sbt /cache/
RUN          mkdir -p /cache/modules/core/src/test/scala/
RUN          echo "object App" > /cache/modules/core/src/test/scala/App.scala
RUN          cd /cache/ && sbt test
RUN          rm -r /cache/

WORKDIR      /app/

ENTRYPOINT   sbt