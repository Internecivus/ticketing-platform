import React from "react";
import App from "next/app";
import Router from "next/router";
import NProgress from "nprogress";
import AppErrorBoundary from "../components/AppErrorBoundary";
import "../css/style.css";
import "nprogress/nprogress.css";

NProgress.configure({
  showSpinner: false,
  speed: 100,
  trickleSpeed: 100,
  trickle: true,
});
Router.events.on("routeChangeStart", () => NProgress.start());
Router.events.on("routeChangeComplete", () => NProgress.done());
Router.events.on("routeChangeError", () => NProgress.done());

class TicketingApp extends App {
  render() {
    const { Component, pageProps } = this.props;
    return (
      <AppErrorBoundary>
        <Component {...pageProps} />
      </AppErrorBoundary>
    );
  }
}

export default TicketingApp;
