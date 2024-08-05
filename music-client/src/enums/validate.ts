const validateName = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("Tên người dùng không được để trống"));
  } else {
    callback();
  }
};

export const validatePassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("Mật khẩu không được để trống"));
  } else {
    callback();
  }
};

export const SignInRules = {
  username: [{ validator: validateName, trigger: "blur", min: 3 }],
  password: [{ validator: validatePassword, trigger: "blur", min: 3 }],
};

export const SignUpRules = {
  username: [{ required: true, trigger: "blur", min: 3 }],
  password: [{ required: true, trigger: "blur", min: 3 }],
  sex: [
    { required: true, message: "Vui lòng chọn giới tính", trigger: "change" },
  ],
  phoneNum: [{ message: "Vui lòng chọn ngày", trigger: "blur" }],
  email: [
    { message: "Vui lòng nhập địa chỉ email", trigger: "blur" },
    {
      type: "email",
      message: "Vui lòng nhập đúng địa chỉ email",
      trigger: ["blur", "change"],
    },
  ],
  birth: [
    {
      required: true,
      type: "date",
      message: "Vui lòng chọn ngày",
      trigger: "change",
    },
  ],
  introduction: [{ message: "Vui lòng nhập giới thiệu", trigger: "blur" }],
  location: [{ message: "Vui lòng nhập địa điểm", trigger: "change" }],
};
