/* eslint-disable */
declare module "*.vue" {
  import type { DefineComponent } from "vue";
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

interface ResponseBody {
  code: integer;
  success: boolean;
  message: string;
  type: string;
  data?: any;
}
