# MVVM (MODEL-VIEW-VIEWMODEL)

- đối với MVC thì thường file quản lý layout (VD: MainActivity) vừa xử lý giao diện, vừa đảm nhận nhiệm vụ Controller xử lý logic
- đối với MVP file quản lý giao diện chỉ xử lý logic thuộc về View, xử lý logic sẽ do 1 class Presenter tự xây dựng xử lý, giữa View và Presenter giao tiếp được với nhau thì phải sử dụng Interface tạo callback
- đối với MVVM, Google đã cung cấp công cụ hỗ trợ là DataBinding và Observable để có thể thiết kế ứng dụng chuẩn xác theo mô hình MVVM

___

## LOGIN APP VỚI MVVM

- ta sẽ thực hiện thiết kế 1 chương trình có giao diện như sau

<img src="https://github.com/hienqp/Ngay047-ArchitecturePattern-MVCTutorial/blob/main/UI_SAMPLE.png">

- với ứng dụng như hình trên, ta có hoạt động của ứng dụng như sau
	- user nhập email, password, sau đó click Login
	- chương trình sẽ xử lý với dữ liệu user nhập vào
	- hiển thị message Login thành công hay thất bại cho user biết

___

## ENABLE DataBinding

- truy cập file build.gradle (Module:app), trong thẻ android ta thêm code enable DataBinding
```js
android {
	//...

    dataBinding {
        enabled = true
    }
}
```

___

## MODEL

- tương tự như 2 mô hình MVC, MVP, trong MVVM thì Model cũng tương tự như vậy, không có sự khác biệt về chức năng
- User.java
```java
public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // kiểm tra chuỗi email có hợp lệ hay không
    public boolean isValidEmail() {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // kiểm tra chuỗi password có hợp lệ hay không
    public boolean isValidPassword() {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
```

___


## VIEWMODEL

- ta sẽ xây dựng 1 class ViewModel extends BaseObservable

- LoginViewModel.java
```java
public class LoginViewModel extends BaseObservable {
	//...
}
```

- trước tiên ta sẽ xây dựng tạm thời 1 ViewModel như trên, để có thể sử dụng DataBinding trong View liên kết đến ViewModel

___

## VIEW

- ở file layout của màn hình (ở đây là MainActivity) ta thiết kế view group bên ngoài chỉ đơn giản là ``layout``
- activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

<!--  -->

</layout>
```

- bên trong view group layout, khai báo 1 thẻ ``data``, trong thẻ ``data`` khai báo 1 thẻ ``variable``  với __name__ (là tên hay tag) và __type__ là đường dẫn đến ViewModel ta đã xây dựng tạm ở trên
```xml
    <data>
        <variable
            name="LoginViewModel"
            type="com.example.mvvmtutorial.LoginViewModel" />
    </data>
```

- sau khi khai báo thẻ ``data``, tiếp tục trong view group ``layout`` ta thiết kế giao diện cho màn hình ứng dụng để người dùng tương tác

- activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="LoginViewModel"
            type="com.example.mvvmtutorial.LoginViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center_horizontal"
        android:orientation="vertical"
        android:padding="16dp"
        tools:context=".MainActivity">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/login"
            android:textColor="@color/black"
            android:textSize="30sp" />

        <EditText
            android:id="@+id/edt_email"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:autofillHints="@string/email"
            android:hint="@string/email"
            android:inputType="textEmailAddress"
            android:textColor="@color/black"
            android:textSize="20sp" />

        <EditText
            android:id="@+id/edt_password"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:autofillHints="@string/password"
            android:hint="@string/password"
            android:inputType="numberPassword"
            android:textColor="@color/black"
            android:textSize="20sp" />

        <TextView
            android:id="@+id/tv_message"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:textSize="18sp"/>

        <Button
            android:id="@+id/btn_login"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:text="@string/login"
            android:textSize="20sp" />

    </LinearLayout>
</layout>
```

___

## LOGIC CỦA VIEWMODEL

- sau khi xây dựng cơ bản giao diện màn hình đã khai báo thẻ dataBinding liên kết đến ViewModel, ta tiến hành xây dựng logic cho ViewModel
- với yêu cầu của màn hình ứng dụng login, người dùng sẽ nhập email và password vào 2 EditText, như vậy ViewModel sẽ lấy 2 dữ liệu này từ View và yêu cầu Model xử lý
- khai báo 2 biến private email và password
- LoginViewModel.java
```java
public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;
}
```

- với 2 biến thành viên email và password ta xây dựng các method getter và setter tương ứng
	- trước method ``get`` ta thêm annotation ``@Bindable``
	- trong method ``set``, sau khi return, gọi method ``notifyPropertyChanged(BR.email);``, tham số truyền vào là ``id`` của trường dữ liệu được thêm vào bởi Bindable (khi thêm @Bindable trước method get)
- LoginViewModel.java
```java
public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
```

___

## RÀNG BUỘC (BINDING) DỮ LIỆU (DATA) ĐẾN VIEWMODEL

- với ViewModel như trên, quay trở lại giao diện màn hình (__activity_main.xml__), ta xử lý ràng buộc dữ liệu vào những method tương ứng trong ViewModel
- khi người dùng nhập email vào EditText email, ViewModel phải nhận được, đồng thời ở View cũng phải reload dữ liệu hiển thị cho người dùng, vì vậy ta sẽ sử dụng ràng buộc dữ liệu 2 chiều đối với thuộc tính ``text`` của EditText email như sau
```xml
<EditText
            android:id="@+id/edt_email"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:autofillHints="@string/email"
            android:hint="@string/email"
            android:inputType="textEmailAddress"
            android:text="@={LoginViewModel.email}"
            android:textColor="@color/black"
            android:textSize="20sp" />
```

- với cú pháp ``@={}`` là ràng buộc dữ liệu 2 chiều
- ``LoginViewModel.email`` là name của variable trỏ đến trường dữ liệu trong BR cần liên kết dữ liệu

- ta làm tương tự với EditText của password
```xml
        <EditText
            android:id="@+id/edt_password"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:autofillHints="@string/password"
            android:hint="@string/password"
            android:inputType="numberPassword"
            android:text="@={LoginViewModel.password}"
            android:textColor="@color/black"
            android:textSize="20sp" />
```

___

## BUTTON LOGIN

- khi người dùng click vào Button LOGIN
	- ViewModel sẽ nhận được dữ liệu email và password
	- ViewModel sẽ làm việc với Model để xử lý dữ liệu
	- ViewModel sẽ trả kết quả về View

- xây dựng method dùng xử lý logic khi người dùng click Button LOGIN trên View
```java
    public void onClickLogin() {
        User user = new User(getEmail(), getPassword());

        if (user.isValidEmail() && user.isValidPassword()) {
            // thông báo khi email và password hợp lệ
        } else {
            // thông báo nếu email hoặc password không đúng
        }
    }
```

- để thông báo cho người dùng login thành công hay thất bại, ta dùng ``ObservableField<Data_type>``, và dùng method ``set()`` hay ``get()`` để truy cập dữ liệu của trường quan sát Observable Field
- và Data_type ở đây là ta cần 1 String do ta sẽ thông báo message cho người dùng
- ta khởi tạo 1 biến ``public`` với kiểu ``ObservableField<Data_type>`` trong ``LoginViewModel``
- trong method ``onClickLogin()`` nếu điều kiện ``true`` thì trường quan sát sẽ ``set()`` 1 chuối thông báo login thành công, nếu ``false`` sẽ ``set()`` chuỗi thông báo login thất bại
```java
public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;

    public ObservableField<String> messageLogin = new ObservableField<>();

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void onClickLogin() {
        User user = new User(getEmail(), getPassword());

        if (user.isValidEmail() && user.isValidPassword()) {
            messageLogin.set("Login Success");
        } else {
            messageLogin.set("Email or Password Invalid");
        }
    }
}
```

- khi người dùng click báo Button LOGIN thì method ``onClickLogin()`` sẽ được gọi, như vậy ở View Button LOGIN ta sẽ thiết lập sự kiện ``onClick`` cho Button LOGIN như sau
- __activity_main.xml__
```xml
        <Button
            android:id="@+id/btn_login"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:onClick="@{() -> LoginViewModel.onClickLogin()}"
            android:text="@string/login"
            android:textSize="20sp" />
```

- còn trường quan sát Obserable Field sẽ được liên kết với TextView dùng để hiển thị message cho người dùng
- __activity_main.xml__
```xml
        <TextView
            android:id="@+id/tv_message"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:text="@{LoginViewModel.messageLogin}"
            android:textSize="18sp"
            android:visibility="visible" />
```

> ở TextView message và Button Login ta chỉ sử dụng ràng buộc dữ liệu 1 chiều ``@{}`` vì
>> TextView chỉ thực hiện việc hiển thị message
>> Button chị thực hiện lắng nghe sự kiện click

### ẨN HIỆN VÀ ĐỔI MÀU TEXTVIEW MESSAGE

- TextView message chỉ xuất hiện khi người dùng click Button Login, và màu sắc của text cũng phải khác nhau tùy theo nội dung, ta làm như sau
- ta vẫn sử dụng trường quan sát Obserable Field nhưng với Data_type khác là ``Boolean``, với Data_type này thì mặc định giá trị là ``false``
- khi method ``onClickLogin()`` được gọi thì ta sẽ ``set(true)`` cho Obserable Field ``isShowMessage``
- nếu email và password hợp lệ thì ta sẽ ``set(true)`` cho Obserable Field ``isSuccess`` ngược lại thì ``set(false)``
```java
public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;
    public ObservableField<String> messageLogin = new ObservableField<>();
    public ObservableField<Boolean> isShowMessage = new ObservableField<>();
    public ObservableField<Boolean> isSuccess = new ObservableField<>();
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void onClickLogin() {
        User user = new User(getEmail(), getPassword());

        isShowMessage.set(true);

        if (user.isValidEmail() && user.isValidPassword()) {
            messageLogin.set("Login Success");
            isSuccess.set(true);
        } else {
            messageLogin.set("Email or Password Invalid");
            isSuccess.set(false);
        }
    }
}
```

- vì Ẩn, Hiện, thiết lập màu cho 1 view nào đó thì nó là do View class quản lý, nên trong file View layout, thẻ ``data`` ta ``import`` 1 ``type`` là ``View``
- __activity_main.xml__
```xml
    <data>
        <import type="android.view.View"/>
        <variable
            name="LoginViewModel"
            type="com.example.mvvmtutorial.LoginViewModel" />
    </data>
```

- còn ở TextView message ta sẽ sử dụng ràng buộc dữ liệu đối với thuộc tính tương ứng đó là ``textColor`` và ``visibility`` như sau
```xml
        <TextView
            android:id="@+id/tv_message"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="30dp"
            android:text="@{LoginViewModel.messageLogin}"
            android:textColor="@{LoginViewModel.isSuccess ? @color/teal_200 : @color/purple_200}"
            android:textSize="18sp"
            android:visibility="@{LoginViewModel.isShowMessage ? View.VISIBLE : View.GONE}" />
```

___

## LIÊN KẾT VIEW VÀ VIEWMODEL

- file MainActivity.java là file điều khiển View, và là file khởi chạy chương trình, ta sẽ dùng nó để khởi tạo và liên kết View đến ViewModel
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LoginViewModel loginViewModel = new LoginViewModel();
        activityMainBinding.setLoginViewModel(loginViewModel);
    }
}
```

___ 

# END