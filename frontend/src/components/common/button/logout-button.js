import { Button } from "primereact/button";
import PropTypes from "prop-types";
import { memo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../../services/user-service";
import history from "../../../store/history";
import LogoutWarningPopup from "../popup/logout-warning-popup";

const LogoutButton = memo(
  (props) => {
    const isLoading = useSelector((state) => state.userReducer.isLoading);
    const [showPopup, setShowPopup] = useState(false);
    const dispatch = useDispatch();

    const onShowPopup = (e) => {
      setShowPopup(true);
      e.preventDefault();
    };

    const onLogout = (e) => {
      dispatch(logout()).then((result) => {
        if (result) {
          setShowPopup(false);
          history.push("/login");
        }
      }).catch(() => {
        // this catch is intentionally left blank
        // because it is already handled by the axios interceptor
      });
    };

    return (
      <>
        <Button
          style={{ width: "100px" }}
          label="Logout"
          icon="pi pi-sign-out"
          onClick={onShowPopup}
          disabled={isLoading || props.disabled}
        />

        <LogoutWarningPopup
          visible={showPopup}
          onHide={() => setShowPopup(false)}
          onReject={() => setShowPopup(false)}
          onApprove={onLogout}
        />
      </>
    );
  },
  (prev, next) => prev.disabled === next.disabled
);

LogoutButton.displayName = "LogoutButton";

LogoutButton.defaultProps = {
  disabled: false,
};

LogoutButton.propTypes = {
  disabled: PropTypes.bool,
};

export default LogoutButton;
