provider "aws" {
  version = "~> 2.0"
  region  = module.project_vars.region
}

provider "cloudflare" {
  version = "~> 2.0"
  email   = "blaz.snuderl@gmail.com"
}
