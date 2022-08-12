export const validateUser = (user) => {
  const validationResult = {
    valid: true,
    fields: {},
  };

  if (!user.username) {
    validationResult.valid = false;
    validationResult.fields = {
      ...validationResult.fields,
      username: "Username cannot be empty",
    };
  }

  if (!user.password) {
    validationResult.valid = false;
    validationResult.fields = {
      ...validationResult.fields,
      password: "Password cannot be empty",
    };
  }

  return validationResult;
};
