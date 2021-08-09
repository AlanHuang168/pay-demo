# pay
pay demo

#回调地址
在application.yml中修改

#环境地址
测试环境地址：https://api-uat.pooul.com
生产环境地址：https://api.pooul.com

#上传公钥
resources/keys/rsa_public.pem
也是这个 ========
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6SNDm4juMTFMJYqGuHCPU7RR70zCZq73Glst8MHLIYZfuHeinVcmFsBTV9WMd9zRZyCLVavoYOhJoFJePtWenq/CGIktnMs8sm2Ksj0V2BLCs3wHM/Z2sqX4SmpAQGKkcEtt4cRxeGJLbqcU8/pJyW/rv+h2ToaLy86KLK4IirK8vRamdkZ7/g9QLuTpP56U6U4ykipqCuzo8RvktKXxunRD7dJT9ncQGe6ewv9sO6zLzd3ibqalpd17Bre3s8j77MIoc4ZaRcz8ul7gm7NMR++vj7Ql5igyMnzyr0MnGQOKioS8Buw9Tu9fP+iJC8QSfS8dPTz6GYN2+lYzqhZyjQIDAQAB
======中间这一行

//平台商户编号是登陆接口返回 : curr_merchant_id
PUT /cms/merchants/#{平台商户编号}/public_key
