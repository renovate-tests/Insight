FROM golang:1.15

WORKDIR /usr/app/scripts

COPY scripts /usr/app/scripts

RUN go mod download
RUN go build -o main

COPY mappings /usr/app/mappings

CMD ["./main"]
