import { Button } from "primereact/button";
import PropTypes from "prop-types";
import { useSelector } from "react-redux";
import Popup from "./popup";

const LogoutWarningPopup = (props) => {
  const isLoading = useSelector((state) => state.userReducer.isLoading);
  return (
    <Popup
      className="popup"
      header="Warning"
      visible={props.visible}
      onHide={props.onHide}
      modal={true}
      dismissableMask={true}
    >
      <div className="grid p-fluid">
        <div className="col-12">
          <p>
            If you log out now, any unsaved change you made will be discarded?
            Do you want to log out?
          </p>
        </div>
        <div className="col-12 md:col-6">
          <Button
            label="Continue"
            icon="pi pi-check"
            disabled={isLoading}
            onClick={(e) => {
              props.onApprove();
              e.preventDefault();
            }}
          />
        </div>
        <div className="col-12 md:col-6">
          <Button
            label="Cancel"
            icon="pi pi-times"
            disabled={isLoading}
            onClick={(e) => {
              props.onReject();
              e.preventDefault();
            }}
          />
        </div>
      </div>
    </Popup>
  );
};

LogoutWarningPopup.propTypes = {
  visible: PropTypes.bool,
  onHide: PropTypes.func,
  onApprove: PropTypes.func,
  onReject: PropTypes.func,
};

export default LogoutWarningPopup;
