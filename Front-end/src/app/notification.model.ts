export interface Notification {
    id: number;
    content: string;
    creation_date: string | Date; // `creation_date` peut être une chaîne ou un objet Date
    user_from_id: number;
    user_to_id: number;
  }