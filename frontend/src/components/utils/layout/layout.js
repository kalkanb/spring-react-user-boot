import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import constants from "../../../store/constants";
import RestCallEmitter from "../../../utils/rest-call-emitter";
import ErrorPopup from "../error/error-popup";

const Layout = (props) => {
  const [visible, setVisible] = useState(false);
  const [message, setMessage] = useState(null);
  const [errorLevel, setErrorLevel] = useState(null);

  useEffect(() => {
    RestCallEmitter.on("response_error", responseErrorListener);
    RestCallEmitter.on("network_error", networkErrorListener);

    return () => {
      RestCallEmitter.removeAllListeners(
        "response_error",
        responseErrorListener
      );
      RestCallEmitter.removeAllListeners("network_error", networkErrorListener);
    };
  }, []);

  const responseErrorListener = (error) => {
    setMessage(error.data);
    let errorType = constants.ErrorTypes.find(
      (c) => c.name === error.data.errorType
    );
    setErrorLevel(
      errorType !== null && errorType !== undefined ? errorType.level : "error"
    );
    setVisible(true);
  };

  const networkErrorListener = (error) => {
    setMessage(error);
    setErrorLevel("error");
    setVisible(true);
  };

  const onHide = () => {
    setVisible(false);
    setMessage(null);
    setErrorLevel(null);
  };

  return (
    <>
      {visible && (
        <ErrorPopup
          onHide={onHide}
          visible={true}
          detail={message}
          level={errorLevel}
        />
      )}
      {props.children}
    </>
  );
};

Layout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default Layout;
