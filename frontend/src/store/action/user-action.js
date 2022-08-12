export const REGISTER_START = "REGISTER_START";
export const REGISTER_END = "REGISTER_END";
export const REGISTER_ERROR = "REGISTER_ERROR";

export const LOGIN_START = "LOGIN_START";
export const LOGIN_END = "LOGIN_END";
export const LOGIN_ERROR = "LOGIN_ERROR";

export const LOGOUT_START = "LOGOUT_START";
export const LOGOUT_END = "LOGOUT_END";
export const LOGOUT_ERROR = "LOGOUT_ERROR";

export const GET_USER_START = "GET_USER_START";
export const GET_USER_END = "GET_USER_END";
export const GET_USER_ERROR = "GET_USER_ERROR";

export const CLEAR_USER_INFO = "CLEAR_USER_INFO";
export const SET_TOKEN = "SET_TOKEN";

export const registerStart = () => ({
  type: REGISTER_START,
});

export const registerEnd = () => ({
  type: REGISTER_END,
});

export const registerError = (error) => ({
  type: REGISTER_ERROR,
  error,
});

export const loginStart = () => ({
  type: LOGIN_START,
});

export const loginEnd = (payload) => ({
  type: LOGIN_END,
  payload,
});

export const loginError = (error) => ({
  type: LOGIN_ERROR,
  error,
});

export const logoutStart = () => ({
  type: LOGOUT_START,
});

export const logoutEnd = () => ({
  type: LOGOUT_END,
});

export const logoutError = (error) => ({
  type: LOGOUT_ERROR,
  error,
});

export const getUserStart = () => ({
  type: GET_USER_START,
});

export const getUserEnd = (payload) => ({
  type: GET_USER_END,
  payload,
});

export const getUserError = (error) => ({
  type: GET_USER_ERROR,
  error,
});

export const clearUserInfo = () => ({
  type: CLEAR_USER_INFO,
});

export const setToken = (payload) => ({
  type: SET_TOKEN,
  payload,
});
