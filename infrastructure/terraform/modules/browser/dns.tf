
resource "cloudflare_zone" "dns" {
    zone = var.domain
}

resource "cloudflare_record" "static" {
  zone_id = cloudflare_zone.dns.id
  name    = local.static_domain
  value   = aws_cloudfront_distribution.s3_distribution.domain_name
  type    = "CNAME"
  ttl     = 1
}

resource "cloudflare_record" "star" {
  zone_id = cloudflare_zone.dns.id
  name    = "*.devtest"
  value   = var.public_ip
  type    = "A"
  ttl     = 1
}

provider "acme" {
  server_url = var.letsencrypt_api_endpoint
}

resource "tls_private_key" "static_private_key" {
  algorithm = "RSA"
}

resource "acme_registration" "email" {
    account_key_pem = tls_private_key.static_private_key.private_key_pem
    email_address   = "blaz.snuderl@gmail.com"
}

resource "acme_certificate" "static_cert" {
  account_key_pem           = acme_registration.reg.account_key_pem
  common_name               = local.static_domain

  dns_challenge {
    provider = "cloudflare"
  }
}

resource "aws_acm_certificate" "static_cert" {
  private_key      =  acme_certificate.static_cert.private_key_pem
  certificate_body = acme_certificate.static_cert.certificate_pem
}
