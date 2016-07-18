openssl req -new -x509 -days 365 \
            -key private-key.pem \
            -config cert.cnf \
            -out certificate.pem
