import {
  getUserEnd,
  getUserError,
  getUserStart,
  loginEnd,
  loginError,
  loginStart,
  logoutEnd,
  logoutError,
  logoutStart,
  registerEnd,
  registerError,
  registerStart,
} from "../store/action/user-action";
import projectClient from "./client/axios-client";

const REST_URL = "/user";

export const register = (user) => {
  const requestUrl = REST_URL + "/register";
  return async (dispatch) => {
    try {
      dispatch(registerStart());
      const result = await projectClient.post(requestUrl, user, {
        unauthorized: true,
      });
      dispatch(registerEnd());
      return result.data;
    } catch (error) {
      dispatch(registerError(error));
      return Promise.reject(error);
    }
  };
};

export const login = (user) => {
  const requestUrl = REST_URL + "/login";
  return async (dispatch) => {
    try {
      dispatch(loginStart());
      const result = await projectClient.post(requestUrl, user, {
        unauthorized: true,
      });
      dispatch(loginEnd(result.data));
      return result.data;
    } catch (error) {
      dispatch(loginError(error));
      return Promise.reject(error);
    }
  };
};

export const logout = () => {
  const requestUrl = REST_URL + "/logout";
  return async (dispatch) => {
    try {
      dispatch(logoutStart());
      const result = await projectClient.delete(requestUrl);

      localStorage.removeItem("X-User-Token");
      localStorage.setItem("Logout-Event", "logout");

      dispatch(logoutEnd());
      return result.status === 204;
    } catch (error) {
      dispatch(logoutError(error));
      return Promise.reject(error);
    }
  };
};

export const getUser = () => {
  return async (dispatch) => {
    try {
      dispatch(getUserStart());
      const result = await projectClient.get(REST_URL);
      dispatch(getUserEnd(result.data));
      return result.data;
    } catch (error) {
      dispatch(getUserError(error));
      return Promise.reject(error);
    }
  };
};
