import React from "react";
import Application from "../app/application";
import { Authentication } from "../app/authentication";
import Path from "../app/path";
import redirect from "../app/isomorphic-redirect";
import hoistNonReactStatic from "hoist-non-react-statics";
import { getCookie } from "../app/utils";

function withAuthentication(authorizedRole, UnprotectedComponent) {
  class ProtectedComponent extends React.Component {
    static async redirectToLogin(response) {
      await redirect(
        {
          pathname: Path.Login,
          query: { redirect: Path[UnprotectedComponent.name] },
        },
        response
      );
    }

    static async handleAuthorizationStatus(authorizationStatus, response) {
      switch (authorizationStatus) {
        case Authentication.Status.NOT_AUTHORIZED:
          response.writeHead(403);
          response.end();
          break;
        case Authentication.Status.NOT_LOGGED_IN:
          await ProtectedComponent.redirectToLogin(response);
          break;
        case Authentication.Status.SUCCESS:
          break;
      }
    }

    static async getInitialProps(context) {
      if (context.req) {
        Authentication.serverSideCookies = context.req.headers.cookie;
      }
      const authorizationStatus = await Authentication.resolveAuthorizationStatus(authorizedRole);
      await ProtectedComponent.handleAuthorizationStatus(authorizationStatus, context.res);
      if (UnprotectedComponent.getInitialProps) {
        return UnprotectedComponent.getInitialProps(context);
      }
      return {};
    }

    render() {
      return <UnprotectedComponent {...this.props} authentication={Authentication} />;
    }
  }
  hoistNonReactStatic(ProtectedComponent, UnprotectedComponent, { getInitialProps: true });
  return ProtectedComponent;
}

export default withAuthentication;
