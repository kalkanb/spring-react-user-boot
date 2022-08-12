import PropTypes from "prop-types";
import { useLayoutEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import useLocalStorage from "../../hooks/useLocalStorage";
import { getUser, logout } from "../../services/user-service";
import { clearUserInfo, setToken } from "../../store/action/user-action";
import history from "../../store/history";

const AuthenticatedPageContainer = (props) => {
  const dispatch = useDispatch();
  const reduxToken = useSelector((state) => state.userReducer.token);
  const [storageToken, setStorageToken] = useLocalStorage("X-User-Token", null);
  const [loadChildren, setLoadChildren] = useState(false);

  window.addEventListener("storage", (event) => {
    if (!!event.storageArea["Logout-Event"]) {
      dispatch(clearUserInfo());
      history.push("/login");
    }
  });

  useLayoutEffect(() => {
    if (
      (!!reduxToken && !storageToken) ||
      (!!reduxToken && !!storageToken && reduxToken !== storageToken)
    ) {
      dispatch(logout())
        .then((result) => {
          if (result) {
            history.push("/login");
          }
        })
        .catch(() => {
          // this catch is intentionally left blank
          // because it is already handled by the axios interceptor
        });;
    } else if (!storageToken && !reduxToken) {
      history.push("/login");
    } else if (!reduxToken && !!storageToken) {
      dispatch(getUser())
        .then(() => {
          dispatch(setToken(storageToken));
          setLoadChildren(true);
        })
        .catch(() => {
          setStorageToken(null);
          dispatch(clearUserInfo());
          history.push("/login");
        });
    } else {
      setLoadChildren(true);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return <>{loadChildren && props.children}</>;
};

AuthenticatedPageContainer.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AuthenticatedPageContainer;
