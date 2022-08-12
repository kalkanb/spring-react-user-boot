import PropTypes from "prop-types";
import { Route } from "react-router-dom";
import Layout from "./layout";

const LayoutRoute = (props) => {
  return (
    <>
      <Layout>
        <Route
          exact={props.exact}
          path={props.path}
          component={props.component}
        />
      </Layout>
    </>
  );
};

LayoutRoute.displayName = "LayoutRoute";

LayoutRoute.defaultProps = {
  exact: false,
};

LayoutRoute.propTypes = {
  exact: PropTypes.bool,
  path: PropTypes.string,
  component: PropTypes.any,
};

export default LayoutRoute;
