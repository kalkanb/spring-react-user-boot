import {
  CLEAR_USER_INFO,
  GET_USER_END,
  GET_USER_ERROR,
  GET_USER_START,
  LOGIN_END,
  LOGIN_ERROR,
  LOGIN_START,
  LOGOUT_END,
  LOGOUT_ERROR,
  LOGOUT_START,
  REGISTER_END,
  REGISTER_ERROR,
  REGISTER_START,
  SET_TOKEN
} from "../action/user-action";

const initialState = {
  user: null,
  token: null,
  isLoading: false,
};

const userReducer = (state = initialState, action = undefined) => {
  switch (action.type) {
    case REGISTER_START:
    case LOGIN_START:
    case LOGOUT_START:
    case GET_USER_START:
      return {
        ...state,
        isLoading: true,
      };
    case LOGIN_END:
      return {
        ...state,
        user: action.payload.user,
        token: action.payload.token,
        isLoading: false,
      };
    case REGISTER_END:
    case LOGOUT_END:
    case CLEAR_USER_INFO:
      return initialState;
    case GET_USER_END:
      return {
        ...state,
        user: action.payload,
        isLoading: false,
      };
    case SET_TOKEN:
      return {
        ...state,
        token: action.payload,
      };
    case REGISTER_ERROR:
    case LOGIN_ERROR:
    case LOGOUT_ERROR:
    case GET_USER_ERROR:
      return {
        ...state,
        isLoading: false,
        error: action.error,
      };
    default:
      return state;
  }
};

export default userReducer;
