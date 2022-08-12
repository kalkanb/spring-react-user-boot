import { Panel } from "primereact/panel";
import PropTypes from "prop-types";
import constants from "../../../store/constants";
import Popup from "../../common/popup/popup";
import  ForbiddenErrorPanel from "./panel/forbidden-error-panel";
import  NetworkErrorPanel from "./panel/network-error-panel";
import  ServerErrorPanel from "./panel/server-error-panel";

const ErrorPopup = (props) => {
  const renderDetail = (item, status) => {
    if (status === 404 && item && item.message === "No message available") {
      item.message = "URL not found!";
    }
    switch (status) {
      case 400:
      case 401:
      case 404:
      case 500:
        return <ServerErrorPanel detail={item} />;
      case 403:
        return <ForbiddenErrorPanel detail={item} />;
      default:
        return <NetworkErrorPanel detail={item} />;
    }
  };

  const renderHeader = (level) => {
    if (level === constants.ErrorLevel.Error) return "Error";
    else if (level === constants.ErrorLevel.Warn) return "Warning";
  };

  const renderClassname = (level) => {
    if (level === constants.ErrorLevel.Error) return "p-panel-error";
    else if (level === constants.ErrorLevel.Warn) return "p-panel-warning";
  };

  return (
    <Popup
      className="error-dialog"
      visible={props.visible}
      onHide={props.onHide}
      modal={true}
    >
      <Panel
        className={renderClassname(props.level)}
        header={renderHeader(props.level)}
      >
        {props.detail && renderDetail(props.detail, props.detail.status)}
      </Panel>
    </Popup>
  );
};

ErrorPopup.displayName = "ErrorPopup";

ErrorPopup.defaultProps = {
  visible: false,
};

ErrorPopup.propTypes = {
  level: PropTypes.oneOf(["error", "warning"]),
  detail: PropTypes.oneOfType([PropTypes.object, PropTypes.string]),
  visible: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
};

export default ErrorPopup;
