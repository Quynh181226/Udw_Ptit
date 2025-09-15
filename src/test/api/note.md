file **test** là quest để test ví dụ khi bấm 
### Đăng ký tài khoản ADMIN
POST http://localhost:8080/api/auth/register => ở bên cạnh từ post sẽ có nút gửi request để test và trả về kết quả
Content-Type: application/json

{
"username": "adminN",
"password": "129012",
"email": "adminN@gmail.com",
"role": "ADMIN"
}
=> 200 là success
<> 2025-09-06T231255.200.json