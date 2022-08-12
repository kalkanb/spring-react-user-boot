import classNames from "classnames";
import { Password } from "primereact/password";
import PropTypes from "prop-types";
import { memo } from "react";

const ProjectPassword = memo(
  (props) => {
    const passwordToolHeader = <h6>Pick a password</h6>;
    const passwordToolFooter = (
      <>
        <p className="mt-2">Suggestions</p>
        <ul className="pl-2 ml-2 mt-0" style={{ lineHeight: "1.5" }}>
          <li>At least one lowercase</li>
          <li>At least one uppercase</li>
          <li>At least one numeric</li>
          <li>Minimum 8 characters</li>
        </ul>
      </>
    );

    return (
      <div className={`${props.className || "col-12"} field`}>
        <label className="input-label" htmlFor={props.id}>
          {props.label}
        </label>
        <Password
          className={classNames("input-text", {
            "bordered-invalid-field": !!props.validationMessage,
          })}
          style={props.inputStyle}
          name={props.prop}
          value={props.value}
          onChange={props.onChange}
          header={passwordToolHeader}
          footer={passwordToolFooter}
          toggleMask={props.toggleMask}
          maxLength={props.maxLength}
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

ProjectPassword.displayName = "ProjectPassword";

ProjectPassword.defaultProps = {
  value: "",
  toggleMask: true,
};

ProjectPassword.propTypes = {
  className: PropTypes.string,
  inputStyle: PropTypes.object,
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  prop: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func.isRequired,
  validationMessage: PropTypes.string,
  maxLength: PropTypes.number,
  toggleMask: PropTypes.bool,
};

export default ProjectPassword;
