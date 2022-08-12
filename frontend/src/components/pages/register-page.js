import { Button } from "primereact/button";
import { Panel } from "primereact/panel";
import { useCallback, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import fireToastMessage from "../../services/toast-call-manager";
import { register } from "../../services/user-service";
import constants from "../../store/constants";
import history from "../../store/history";
import { validateUser } from "../../validator/user-validator";
import ProjectInputText from "../common/input/project-input-text";
import ProjectPassword from "../common/input/project-password";

const RegisterPage = () => {
  const dispatch = useDispatch();
  const isLoading = useSelector((state) => state.userReducer.isLoading);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [validation, setValidation] = useState(null);

  const usernameOnChange = useCallback((event) => {
    setUsername(event.target.value);
  }, []);

  const passwordOnChange = useCallback((event) => {
    setPassword(event.target.value);
  }, []);

  const handleRegister = (e) => {
    const user = { username, password };
    const validationResult = validateUser(user);
    if (!validationResult.valid) {
      setValidation(validationResult.fields);
    } else {
      dispatch(register(user))
        .then((result) => {
          if (result) {
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
            error?.data?.status === 400 &&
            (error?.data?.errorType ===
              constants.Exceptions.ProjectArgumentValidationException ||
              error?.data?.errorType ===
                constants.Exceptions.MethodArgumentNotValidException)
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

  const panelHeader = <h4 className="auth-panel-header">Register</h4>;

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
                <a disabled={isLoading} href="/login">
                  Login
                </a>
              </div>
              <div className="col-12 col-lg-6">
                <Button
                  style={{ width: "100%" }}
                  label="Register"
                  icon="pi pi-user-plus"
                  disabled={isLoading}
                  onClick={handleRegister}
                />
              </div>
            </div>
          </div>
        </div>
      </Panel>
    </div>
  );
};

export default RegisterPage;
