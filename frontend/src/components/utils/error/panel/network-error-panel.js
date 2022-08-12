import PropTypes from "prop-types";

const NetworkErrorPanel = (props) => {
  return (
    <div className="grid p-fluid">
      <div className="col-12">
        <label className="big-bold">Message: </label>
        {props.detail.message}
      </div>
      <div className="col-12">
        <label className="big-bold">Detail: </label>
        {props.detail.stack}
      </div>
    </div>
  );
};

NetworkErrorPanel.displayName = "NetworkErrorPanel";

NetworkErrorPanel.propTypes = {
  detail: PropTypes.object,
};

export default NetworkErrorPanel;
