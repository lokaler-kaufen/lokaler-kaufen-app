export interface PageObject {

  /**
   * Navigates the browser to the given URL.
   *
   * Returns a promise that should be resolved when the navigation was successful.
   */
  get(url: string): Promise<unknown>;

}
