import { Button } from "primereact/button";
import PropTypes from "prop-types";
import { useDispatch } from "react-redux";
import { clearUserInfo } from "../../../store/action/user-action";
import history from "../../../store/history";
import error from "../../../style/images/error.svg";

const ErrorDetail = (props) => {
  const dispatch = useDispatch();
  const sendHome = (event) => {
    dispatch(clearUserInfo());
    localStorage.removeItem("X-User-Token");
    history.push("/");
    event.preventDefault();
  };

  return (
    <div className="exception-body error-page">
      <div className="exception-type">
        <img src={error} alt="Unexpected error" />
      </div>
      <div className="card exception-panel">
        <i className="material-icons">error</i>
        <h1>An Error Occured</h1>
        <div className="exception-detail">
          <h3>Message</h3>
          {JSON.stringify(props.error.message)}
        </div>
        <Button
          style={{ width: "100%" }}
          label="Homepage"
          icon="pi pi-home"
          onClick={sendHome}
        />
      </div>
    </div>
  );
};

ErrorDetail.propTypes = {
  error: PropTypes.object,
  componentStack: PropTypes.string,
};

export default ErrorDetail;
