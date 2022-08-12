import ApplicationEventEmitter from "../utils/application-emitter";

const fireToastMessage = (toastMessage) => {
  ApplicationEventEmitter.emit("toast_message", toastMessage);
};

export default fireToastMessage;
