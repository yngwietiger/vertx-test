openssl req -new -x509 -days 365 \
            -key file.pem \
            -config cert2.cnf \
            -out certificate.pem
