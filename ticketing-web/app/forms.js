import * as yup from "yup";

let isRequired = "This field is required";

const Forms = {
  Category: {
    defaultValues: { name: "" },
    schema: yup.object({
      name: yup.string().max(120).required(isRequired),
    }),
  },
  User: {
    defaultValues: { username: "", email: "", groups: [] },
    schema: yup.object({
      username: yup.string().min(5).max(120).required(isRequired),
      email: yup.string().min(5).max(120).email().required(isRequired),
      groups: yup.array().required(isRequired),
    }),
  },
  Login: {
    defaultValues: { usernameOrEmail: "", password: "" },
    schema: yup.object({
      usernameOrEmail: yup.string().required(isRequired),
      password: yup.string().required(isRequired),
    }),
  },
  ForgotPassword: {
    defaultValues: { email: "" },
    schema: yup.object({
      email: yup.string().email().required(isRequired),
    }),
  },
  ChangePassword: {
    defaultValues: { oldPassword: "", newPassword: "", newPasswordRepeat: "" },
    schema: yup.object({
      oldPassword: yup.string().required(isRequired),
      newPassword: yup.string().required(isRequired),
      newPasswordRepeat: yup
        .string()
        .required(isRequired)
        .oneOf([yup.ref("newPassword"), null], "Passwords do not match!"),
    }),
  },
  Ticket: {
    defaultValues: {
      title: "",
      content: "",
      category: null,
      priority: null,
      assignedUser: null,
    },
    schema: yup.object({
      title: yup.string().max(240).required(isRequired),
      content: yup.string().max(16000).required(isRequired),
      category: yup.number().typeError(isRequired).required(isRequired),
      priority: yup.number().typeError(isRequired).required(isRequired),
      assignedUser: yup.number().notRequired().nullable(),
    }),
  },
};

export default Forms;
