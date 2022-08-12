const constants = {
  ErrorLevel: {
    Error: "error",
    Warn: "warning",
  },
  ErrorTypes: [
    {
      name: "ProjectException",
      level: "error",
    },
    {
      name: "ProjectValidationException",
      level: "warning",
    },
    {
      name: "ProjectNotFoundException",
      level: "error",
    },
    {
      name: "MethodArgumentNotValidException",
      level: "warning",
    },
  ],
  Exceptions: {
    MethodArgumentNotValidException: "MethodArgumentNotValidException",
    ProjectArgumentValidationException: "ProjectArgumentValidationException",
    ProjectAuthenticationValidationException:
      "ProjectAuthenticationValidationException",
  },
};

export default constants;
