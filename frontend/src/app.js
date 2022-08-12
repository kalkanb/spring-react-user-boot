import { useSelector } from "react-redux";
import { Router, Switch } from "react-router";
import ProjectToast from "./components/common/project-toast";
import ScreenLoader from "./components/common/screen-loader";
import HomePage from "./components/pages/home-page";
import LoginPage from "./components/pages/login-page";
import NotFoundPage from "./components/pages/not-found-page";
import RegisterPage from "./components/pages/register-page";
import LayoutRoute from "./components/utils/layout/layout-route";
import history from "./store/history";

const App = () => {
  const isLoading = useSelector((state) => state.userReducer.isLoading);
  return (
    <>
      <ProjectToast />
      <Router history={history}>
        <Switch>
          <LayoutRoute exact path="/" component={HomePage} />
          <LayoutRoute exact path="/register" component={RegisterPage} />
          <LayoutRoute exact path="/login" component={LoginPage} />
          <LayoutRoute path="*" component={NotFoundPage} />
        </Switch>
      </Router>
      {isLoading && <ScreenLoader />}
    </>
  );
};

export default App;
