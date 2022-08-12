import { Toast } from "primereact/toast";
import { useEffect, useRef, useState } from "react";
import ReactDOM from "react-dom";
import ApplicationEventEmitter from "../../utils/application-emitter";

const ProjectToast = () => {
  const [portalElement] = useState(() => document.createElement("div"));
  const toast = useRef();

  const listenMessage = (message) => {
    toast.current.show(message);
  };

  useEffect(() => {
    const notificationRoot = document.getElementById("toast-root");
    ApplicationEventEmitter.on("toast_message", listenMessage);
    notificationRoot.appendChild(portalElement);
    return () => {
      ApplicationEventEmitter.removeListener("toast_message", listenMessage);
      notificationRoot.removeChild(portalElement);
    };
  }, [portalElement]);

  return ReactDOM.createPortal(
    <Toast ref={toast} baseZIndex={9999} />,
    portalElement
  );
};

export default ProjectToast;
