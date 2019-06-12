# HandyAC
[Speech Processing Project] HandyAC - an Android app enables users to control air-conditioners by voice commands.

### Mô tả:

- HandyAC cho phép người dùng điều khiển điều hòa trong phòng bằng giọng nói. Ứng dụng khả dụng khi tích hợp vào hệ thống điều khiển điều hòa thông minh (IoT project).

- Ứng dụng HandyAC kết nối với hệ thống điều khiển điều hòa qua giao thức MQTT (nhận hay gửi dữ liệu đều thông qua giao thức này) và phần nhận dạng giọng nói sử dụng Speech Recognition API của Google;

### Tóm tắt về họat động của hệ thống:
Trong hệ thống điều khiển điều hòa, có 1 thành phần gọi là HomeGateway, được cài đặt cả giao thức Echonet Lite và MQTT. HomeGateway qua giao thức Echonet Lite sẽ trao đổi (gửi/nhận) dữ liệu với thiết bị phần cứng, với dữ liệu nhận được HomeGateway sẽ đẩy tới MQTT Broker qua giao thức MQTT để cho các thành phần khác như Database, Web App hay Mobile App nhận dữ liệu. Ngược lại, để điều khiển được thiết bị, lệnh điều khiển sẽ được gửi đi từ Mobile App hoặc Web App tới MQTT Broker để HomeGateway nhận và chuyển tới phần cứng. Tất cả các gói dữ liệu được đóng gói dưới dạng JSON.

### Các tính năng chính:

+ Đăng nhập với **username**  & **password** của MQTT Broker để có thể cho phép app kết nối tới MQTT Broker. Địa chỉ MQTT Broker hiện tại app đang sử dụng là: **tcp://k61iotlab.duckdns.org:1883** (MQTT Broker được cài đặt trên thiết bị Raspberry Pi tại nhà);
+ Nhận các thông tin về thiết bị phần cứng bao gồm nhiệt độ điều hòa đang được set, nhiệt độ & độ ẩm thực tế trong phòng (giá trị lấy từ cảm biến nhiệt độ - độ ẩm) và xem có người trong nhà hay không (giá trị lấy từ cảm biến hồng ngoại phát hiện người) ;
+ Gửi lệnh điều khiển điều hòa bằng giọng nói: khi người dùng nói, app HandyAC sẽ chuyển giọng nói thành văn bản, sau đó xử lý chuỗi văn bản này, xác nhận lệnh điều khiển và gửi đi lệnh điều khiển bằng 1 file JSON;
+ Cho phép người dùng sửa topic MQTT để gửi/nhận dữ liệu;
+ Cho phép người dùng xem thông tin chủ sở hữu, mã nhà, phòng lắp thiết bị điều hòa và địa chỉ MAC của phần cứng;
+ Cho phép người dùng gửi email phản hồi về ứng dụng;

### Clip demo ứng dụng HandyAC:
- https://youtu.be/WgUp-BVN9vc
