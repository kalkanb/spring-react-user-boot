import { Dialog } from "primereact/dialog";
import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import ReactDOM from "react-dom";

const Popup = (props) => {
  const { children, ...rest } = props;
  const [portalElement] = useState(() => document.createElement("div"));

  useEffect(() => {
    const noticationRoot = document.getElementById("dialog-root");
    noticationRoot.appendChild(portalElement);
    return () => {
      noticationRoot.removeChild(portalElement);
    };
  }, [portalElement]);

  return ReactDOM.createPortal(
    <Dialog {...rest} contentClassName={props.className}>
      {children}
    </Dialog>,
    portalElement
  );
};

Popup.defaultProps = {
  draggable: false,
  resizable: false,
};

Popup.propTypes = {
  children: PropTypes.node.isRequired,
  className: PropTypes.string,
  header: PropTypes.string,
  visible: PropTypes.bool,
  onHide: PropTypes.func,
  modal: PropTypes.bool,
  dismissableMask: PropTypes.bool,
  closable: PropTypes.bool,
  closeOnEscape: PropTypes.bool,
  icons: PropTypes.func,
  draggable: PropTypes.bool,
  resizable: PropTypes.bool,
};

export default Popup;