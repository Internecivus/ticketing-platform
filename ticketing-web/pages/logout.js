import React from "react";
import { Authentication } from "../app/authentication";
import Path from "../app/path";
import withAuthentication from "../components/withAuthentication";
import redirect from "../app/isomorphic-redirect";

class Logout extends React.Component {
  static async getInitialProps(context) {
    await Authentication.logout();
    await redirect(Path.Login, context.res);
  }

  render() {
    return null;
  }
}

export default withAuthentication(null, Logout);
