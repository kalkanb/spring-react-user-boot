import { Button } from "primereact/button";
import { Panel } from "primereact/panel";
import { useCallback, useLayoutEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import useLocalStorage from "../../hooks/useLocalStorage";
import fireToastMessage from "../../services/toast-call-manager";
import { getUser, login } from "../../services/user-service";
import { clearUserInfo, setToken } from "../../store/action/user-action";
import constants from "../../store/constants";
import history from "../../store/history";
import { validateUser } from "../../validator/user-validator";
import ProjectInputText from "../common/input/project-input-text";
import ProjectPassword from "../common/input/project-password";

const LoginPage = () => {
  const dispatch = useDispatch();
  const isLoading = useSelector((state) => state.userReducer.isLoading);
  const reduxToken = useSelector((state) => state.userReducer.token);
  const [storageToken, setStorageToken] = useLocalStorage("X-User-Token", null);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loggedIn, setLoggedIn] = useState(false);
  const [validation, setValidation] = useState(null);

  window.addEventListener("storage", (event) => {
    if (!loggedIn) {
      if (!!event.storageArea["X-User-Token"]) {
        dispatch(getUser())
          .then(() => {
            dispatch(setToken(event.storageArea["X-User-Token"]));
            history.push("/");
          })
          .catch(() => {
            setStorageToken(null);
            dispatch(clearUserInfo());
          });
      }
    }
  });

  useLayoutEffect(() => {
    if (
      (!storageToken && !!reduxToken) ||
      (!!reduxToken && !!storageToken && reduxToken !== storageToken)
    ) {
      dispatch(clearUserInfo());
    } else if (!!reduxToken && !!storageToken && reduxToken === storageToken) {
      history.push("/");
    } else if (!!storageToken) {
      dispatch(getUser())
        .then(() => {
          dispatch(setToken(storageToken));
          history.push("/");
        })
        .catch(() => {
          setStorageToken(null);
          dispatch(clearUserInfo());
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const usernameOnChange = useCallback((event) => {
    setUsername(event.target.value);
  }, []);

  const passwordOnChange = useCallback((event) => {
    setPassword(event.target.value);
  }, []);

  const handleLogin = (e) => {
    const user = { username, password };
    const validationResult = validateUser(user);
    if (!validationResult.valid) {
      setValidation(validationResult.fields);
    } else {
      dispatch(login(user))
        .then((result) => {
          if (result) {
            setLoggedIn(true);
            localStorage.removeItem("Logout-Event");
            setStorageToken(result.token);
            fireToastMessage({
              severity: "success",
              summary: "Success",
              detail: "Successfully Registered",
              life: 3000,
            });
            history.push("/");
          }
        })
        .catch((error) => {
          if (
            (error?.data?.status === 400 || error?.data?.status === 401) &&
            (error?.data?.errorType ===
              constants.Exceptions.ProjectArgumentValidationException ||
              error?.data?.errorType ===
                constants.Exceptions.MethodArgumentNotValidException ||
              error?.data?.errorType ===
                constants.Exceptions.ProjectAuthenticationValidationException)
          ) {
            setValidation(error.data.fields);
            fireToastMessage({
              severity: "warn",
              summary: "Warning",
              detail: "Correct the invalid fields",
              life: 3000,
            });
          }
        });
    }
    e.preventDefault();
  };

  const panelHeader = <h4 className="auth-panel-header">Login</h4>;

  return (
    <div className="vertical-center card small-card clearfix">
      <Panel header={panelHeader}>
        <div className="grid p-fluid">
          <ProjectInputText
            id="username"
            label="Username"
            inputStyle={{ height: "50px" }}
            prop="username"
            value={username}
            maxLength={255}
            onChange={usernameOnChange}
            validationMessage={(!!validation && validation.username) || null}
          />
          <ProjectPassword
            id="password"
            label="Password"
            prop="password"
            inputStyle={{ height: "50px" }}
            value={password}
            onChange={passwordOnChange}
            toggleMask={true}
            maxLength={16}
            validationMessage={(!!validation && validation.password) || null}
          />

          <div className="col-12 field">
            <div className="grid p-fluid">
              <div className="col-12 col-lg-6">
                <a disabled={isLoading} href="/register">
                  Register
                </a>
              </div>
              <div className="col-12 col-lg-6">
                <Button
                  style={{ width: "100%" }}
                  label="Login"
                  icon="pi pi-user-plus"
                  disabled={isLoading}
                  onClick={handleLogin}
                />
              </div>
            </div>
          </div>
        </div>
      </Panel>
    </div>
  );
};

export default LoginPage;
