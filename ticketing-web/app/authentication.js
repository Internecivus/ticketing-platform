import App from "next/app";
import Application from "./application";
import Storage from "./storage";
import { AuthenticationAPI, notifyIfError, UserAPI } from "./api";
import { getCookie, removeCookie } from "./utils";

export const Authentication = new (class {
  RememberMeTokenName = "JREMEMBERMEID";

  Status = {
    NOT_LOGGED_IN: "NOT_LOGGED_IN",
    NOT_AUTHORIZED: "NOT_AUTHORIZED",
    SUCCESS: "SUCCESS",
  };

  get currentUser() {
    if (Application.isBrowser) {
      return Storage.get(Storage.CURRENT_USER);
    } else {
      return this._currentUser;
    }
  }

  set currentUser(currentUser) {
    if (Application.isBrowser) {
      Storage.set(Storage.CURRENT_USER, currentUser);
    } else {
      this._currentUser = currentUser;
    }
  }

  get serverSideCookies() {
    return this._serverSideCookies;
  }

  set serverSideCookies(serverSideCookies) {
    this._serverSideCookies = serverSideCookies;
  }

  async resolveUser() {
    let user = this.currentUser;

    if (!user) {
      try {
        user = await UserAPI.getCurrentUser();
      } catch (e) {
        user = null;

        if (this.isLoggedIn()) {
          this.clearRememberMeCookie();
        }
      }
    }
    return user;
  }

  async resolveAuthorizationStatus(authorizedRole) {
    if (!this.currentUser) {
      this.currentUser = await this.resolveUser();
    }

    if (!authorizedRole) {
      return this.Status.SUCCESS;
    } else if (!this.isLoggedIn()) {
      return this.Status.NOT_LOGGED_IN;
    } else if (this.hasRole(authorizedRole)) {
      return this.Status.SUCCESS;
    }
    return this.Status.NOT_AUTHORIZED;
  }

  isLoggedIn() {
    return !!getCookie(
      this.RememberMeTokenName,
      Application.isBrowser ? null : this.serverSideCookies
    );
  }

  clearRememberMeCookie() {
    if (Application.isBrowser) {
      removeCookie(this.RememberMeTokenName);
    } else {
      this.serverSideCookies = removeCookie(this.RememberMeTokenName, this.serverSideCookies);
    }
  }

  hasRole(roleName) {
    return this.currentUser.roles.find((role) => role.name === roleName);
  }

  async login(username, password) {
    if (this.isLoggedIn()) {
      return;
    }
    await AuthenticationAPI.login(username, password);
    await this.resolveUser();
  }

  async logout() {
    if (!this.isLoggedIn()) {
      return;
    }

    await AuthenticationAPI.logout();

    this.clearUser();
  }

  clearUser() {
    this.currentUser = null;
  }
})();

export const role = {
  CATEGORY_ADMINISTRATION: "CATEGORY_ADMINISTRATION",
  USER_ADMINISTRATION: "USER_ADMINISTRATION",
  CATEGORY_USER_ASSIGNMENT: "CATEGORY_USER_ASSIGNMENT",
  TICKET_ADMINISTRATION: "TICKET_ADMINISTRATION",
};
