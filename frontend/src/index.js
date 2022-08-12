import "primeflex/primeflex.css";
import "primeicons/primeicons.css";
import "primereact/resources/primereact.min.css";
import "primereact/resources/themes/mdc-light-indigo/theme.css";
import { StrictMode, Suspense } from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import App from "./app";
import ScreenLoader from "./components/common/screen-loader";
import ErrorBoundary from "./components/utils/error/error-boundary";
import store from "./store/configure-store";
import "./style/index.css";
import "./style/layout.css";

ReactDOM.render(
  <StrictMode>
    <Provider store={store}>
      <ErrorBoundary>
        <Suspense fallback={<ScreenLoader />}>
          <App />
        </Suspense>
      </ErrorBoundary>
    </Provider>
  </StrictMode>,
  document.getElementById("app-root")
);
