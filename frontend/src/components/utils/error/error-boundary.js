import PropTypes from "prop-types";
import React from "react";
import ErrorDetail from "./error-detail";

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      hasError: false,
      error: null,
      errorInfo: null,
    };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    this.setState((prevState) => {
      return {
        ...prevState,
        error,
        errorInfo,
      };
    });
  }

  render() {
    if (this.state.hasError && this.state.error && this.state.errorInfo) {
      return (
        <ErrorDetail
          componentStack={this.state.errorInfo.componentStack}
          error={this.state.error}
        />
      );
    }

    return this.props.children || null;
  }
}

ErrorBoundary.propTypes = {
  children: PropTypes.element,
};
