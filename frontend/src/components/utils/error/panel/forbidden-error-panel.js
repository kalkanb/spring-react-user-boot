import PropTypes from "prop-types";

const ForbiddenErrorPanel = (props) => {
  return (
    <div className="grid p-fluid">
      <div className="col-12">
        <label className="big-bold">Message: </label>
        Unauthorized
      </div>
      <div className="col-12">
        <label className="big-bold">URL: </label>
        {props.detail.path}
      </div>
    </div>
  );
};

ForbiddenErrorPanel.displayName = "ForbiddenErrorPanel";

ForbiddenErrorPanel.propTypes = {
  detail: PropTypes.object,
};

export default ForbiddenErrorPanel;
