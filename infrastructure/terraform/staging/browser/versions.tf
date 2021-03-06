terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.17.0"
    }
    github = {
      source  = "hashicorp/github"
      version = "3.1.0"
    }
    tls = {
      source  = "hashicorp/tls"
      version = "3.0.0"
    }
  }
  required_version = ">= 0.13"
}
