import Application from "./application";
import redirect from "./isomorphic-redirect";

class Navigation {
  static async redirectBackTo(redirectUrl, response) {
    if (redirectUrl) {
      await redirect(redirectUrl, response);
    } else {
      await redirect(Application.homepage, response);
    }
  }
}

export default Navigation;
