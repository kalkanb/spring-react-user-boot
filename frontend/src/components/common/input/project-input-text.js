import classNames from "classnames";
import { InputText } from "primereact/inputtext";
import PropTypes from "prop-types";
import { memo } from "react";

const ProjectInputText = memo(
  (props) => {
    return (
      <div className={`${props.className || "col-12"} field`}>
        <label className="input-label" htmlFor={props.id}>
          {props.label}
        </label>
        <InputText
          style={props.inputStyle}
          className={classNames("input-text", {
            "bordered-invalid-field": !!props.validationMessage,
          })}
          name={props.prop}
          value={props.value}
          maxLength={props.maxLength}
          onChange={props.onChange}
        />
        <small className="field-validation-message">
          {props.validationMessage}
        </small>
      </div>
    );
  },
  (prev, next) =>
    prev.value === next.value &&
    prev.validationMessage === next.validationMessage &&
    prev.onChange === next.onChange
);

ProjectInputText.displayName = "ProjectInputText";

ProjectInputText.defaultProps = {
  value: "",
};

ProjectInputText.propTypes = {
  className: PropTypes.string,
  inputStyle: PropTypes.object,
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  prop: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func.isRequired,
  validationMessage: PropTypes.string,
  maxLength: PropTypes.number,
};

export default ProjectInputText;
