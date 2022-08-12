import axios from "axios";
import constants from "../../store/constants";
import RestCallEmitter from "../../utils/rest-call-emitter";
import store from "../../store/configure-store";

const getClient = (options = {}) => {
  const catchableExceptions = [
    constants.Exceptions.MethodArgumentNotValidException,
    constants.Exceptions.ProjectArgumentValidationException,
    constants.Exceptions.ProjectAuthenticationValidationException,
  ];

  const client = axios.create(options);

  client.interceptors.request.use(
    (config) => {
      const { unauthorized, ...axiosConfig } = config;
      if (!unauthorized) {
        let token = store.getState().userReducer.token || null;
        if (!token) {
          token = localStorage.getItem("X-User-Token") || null;
        }
        if (!!token && token !== "null" && token !== "undefined") {
          axiosConfig.headers["X-User-Token"] = token;
        }
      }

      console.info("Axios Interceptor Request", axiosConfig);
      return axiosConfig;
    },
    (error) => {
      console.error("Axios Interceptor Request: ", error);
      return Promise.reject(error);
    }
  );

  client.interceptors.response.use(
    (response) => {
      console.info("Axios Interceptor Response : ", response);
      if (response.data.errorType) {
        let responseError = {
          ...response,
          data: { ...response.data },
        };
        RestCallEmitter.emit("response_error", responseError);
        return Promise.reject(response.data);
      }
      return response;
    },
    (error) => {
      if (error.response && error.response.data) {
        console.error("Axios Interceptor Response : ", error.response);

        let responseError = {
          ...error.response,
          data: {
            ...error.response.data,
          },
        };

        if (!catchableExceptions.includes(error.response.data.errorType)) {
          RestCallEmitter.emit("response_error", responseError);
        }

        return Promise.reject(error.response);
      }

      console.log(error.toJSON());
      RestCallEmitter.emit("network_error", error.toJSON());
      return Promise.reject(error.toJSON());
    }
  );

  return client;
};

export default getClient;
