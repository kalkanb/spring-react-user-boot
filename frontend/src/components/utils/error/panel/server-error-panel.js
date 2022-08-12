import PropTypes from "prop-types";

const ServerErrorPanel = (props) => {
  return (
    <div className="grid p-fluid">
      <div className="col-12">
        <label className="big-bold">Http Status: </label>
        {props.detail.status}
      </div>
      <div className="col-12">
        <label className="big-bold">Error Type: </label>
        {props.detail.errorType}
      </div>
      <div className="col-12">
        <label className="big-bold">Message: </label>
        {props.detail.message}
      </div>
      <div className="col-12">
        <label className="big-bold">URL: </label>
        {props.detail.path}
      </div>
    </div>
  );
};

ServerErrorPanel.displayName = "ServerErrorPanel";

ServerErrorPanel.propTypes = {
  detail: PropTypes.object,
};

export default ServerErrorPanel;
