import { api } from "./baseApi";
const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    openFact: build.mutation<OpenFactApiResponse, OpenFactApiArg>({
      query: (queryArg) => ({
        url: `/api/characters/open_fact/${queryArg.characterId}/${queryArg.factType}`,
        method: "PUT",
      }),
    }),
    vote: build.mutation<VoteApiResponse, VoteApiArg>({
      query: (queryArg) => ({
        url: `/api/room/${queryArg.roomId}/vote`,
        method: "POST",
        params: { characterId: queryArg.characterId },
      }),
    }),
    startGame: build.mutation<StartGameApiResponse, StartGameApiArg>({
      query: (queryArg) => ({
        url: `/api/room/${queryArg.roomId}/start`,
        method: "POST",
      }),
    }),
    createPool: build.mutation<CreatePoolApiResponse, CreatePoolApiArg>({
      query: (queryArg) => ({
        url: `/api/room/${queryArg.roomId}/create_pool`,
        method: "POST",
      }),
    }),
    joinRoom: build.mutation<JoinRoomApiResponse, JoinRoomApiArg>({
      query: (queryArg) => ({
        url: `/api/room/join/${queryArg.joinCode}`,
        method: "POST",
      }),
    }),
    createRoom: build.mutation<CreateRoomApiResponse, CreateRoomApiArg>({
      query: () => ({ url: `/api/room/createRoom`, method: "POST" }),
    }),
    createCharacter: build.mutation<
      CreateCharacterApiResponse,
      CreateCharacterApiArg
    >({
      query: (queryArg) => ({
        url: `/api/characters/create`,
        method: "POST",
        body: queryArg.createCharacterRequest,
      }),
    }),
    getGameHistory: build.query<
      GetGameHistoryApiResponse,
      GetGameHistoryApiArg
    >({
      query: () => ({ url: `/api/room` }),
    }),
    getRoomState: build.query<GetRoomStateApiResponse, GetRoomStateApiArg>({
      query: (queryArg) => ({ url: `/api/room/${queryArg.roomId}` }),
    }),
    getPolls: build.query<GetPollsApiResponse, GetPollsApiArg>({
      query: (queryArg) => ({ url: `/api/room/${queryArg.roomId}/all_polls` }),
    }),
    getApiMe: build.query<GetApiMeApiResponse, GetApiMeApiArg>({
      query: () => ({ url: `/api/me` }),
    }),
    getCharacterById: build.query<
      GetCharacterByIdApiResponse,
      GetCharacterByIdApiArg
    >({
      query: (queryArg) => ({ url: `/api/characters/${queryArg.id}` }),
    }),
    getOpenedFacts: build.query<
      GetOpenedFactsApiResponse,
      GetOpenedFactsApiArg
    >({
      query: (queryArg) => ({
        url: `/api/characters/get_opened`,
        params: { openedFactsRequest: queryArg.openedFactsRequest },
      }),
    }),
    getAllFacts: build.query<GetAllFactsApiResponse, GetAllFactsApiArg>({
      query: (queryArg) => ({
        url: `/api/characters/get_all_facts/${queryArg.characterId}`,
      }),
    }),
    generateFact: build.query<GenerateFactApiResponse, GenerateFactApiArg>({
      query: (queryArg) => ({
        url: `/api/characters/generate_facts/${queryArg.roomId}`,
      }),
    }),
    getAllRooms: build.query<GetAllRoomsApiResponse, GetAllRoomsApiArg>({
      query: () => ({ url: `/api/admin/rooms` }),
    }),
  }),
  overrideExisting: false,
});
export { injectedRtkApi as studyGroupApi };
export type OpenFactApiResponse = /** status 200 OK */ FactResponse;
export type OpenFactApiArg = {
  characterId: number;
  factType:
    | "BODY_TYPE"
    | "HEALTH"
    | "TRAIT"
    | "HOBBY"
    | "PROFESSION"
    | "PHOBIA"
    | "EQUIPMENT"
    | "BAG";
};
export type VoteApiResponse = /** status 200 OK */ PollMessage;
export type VoteApiArg = {
  roomId: number;
  characterId: number;
};
export type StartGameApiResponse = /** status 200 OK */ RoomMessage;
export type StartGameApiArg = {
  roomId: number;
};
export type CreatePoolApiResponse = /** status 200 OK */ PollMessage;
export type CreatePoolApiArg = {
  roomId: number;
};
export type JoinRoomApiResponse = /** status 200 OK */ RoomMessage;
export type JoinRoomApiArg = {
  joinCode: string;
};
export type CreateRoomApiResponse = /** status 200 OK */ RoomMessage;
export type CreateRoomApiArg = void;
export type CreateCharacterApiResponse = /** status 200 OK */ CharacterResponse;
export type CreateCharacterApiArg = {
  createCharacterRequest: CreateCharacterRequest;
};
export type GetGameHistoryApiResponse = /** status 200 OK */ RoomMessage[];
export type GetGameHistoryApiArg = void;
export type GetRoomStateApiResponse = /** status 200 OK */ RoomMessage;
export type GetRoomStateApiArg = {
  roomId: number;
};
export type GetPollsApiResponse = /** status 200 OK */ PollMessage[];
export type GetPollsApiArg = {
  roomId: number;
};
export type GetApiMeApiResponse = /** status 200 OK */ User;
export type GetApiMeApiArg = void;
export type GetCharacterByIdApiResponse =
  /** status 200 OK */ CharacterResponse;
export type GetCharacterByIdApiArg = {
  id: number;
};
export type GetOpenedFactsApiResponse =
  /** status 200 OK */ OpenedFactsResponse;
export type GetOpenedFactsApiArg = {
  openedFactsRequest: OpenedFactsRequest;
};
export type GetAllFactsApiResponse = /** status 200 OK */ AllFactsResponse;
export type GetAllFactsApiArg = {
  characterId: number;
};
export type GenerateFactApiResponse = /** status 200 OK */ GenerateFactResponse;
export type GenerateFactApiArg = {
  roomId: number;
};
export type GetAllRoomsApiResponse = /** status 200 OK */ RoomMessage[];
export type GetAllRoomsApiArg = void;
export type FactResponse = {
  name?: string;
};
export type User = {
  id?: number;
  login?: string;
  username?: string;
};
export type OpenedFacts = {
  character_id?: number;
  bodyType?: string;
  health?: string;
  trait?: string;
  hobby?: string;
  profession?: string;
  phobia?: string;
  equipment?: string;
  bag?: string;
};
export type CharacterPrivateMessage = {
  id?: number;
  name?: string;
  age?: number;
  sex?:
    | "\u041C\u0443\u0436\u0447\u0438\u043D\u0430"
    | "\u0416\u0435\u043D\u0449\u0438\u043D\u0430";
  notes?: string;
  isActive?: boolean;
  user?: User;
  openedFacts?: OpenedFacts;
};
export type VoteMessage = {
  id?: number;
  targetCharacter?: CharacterPrivateMessage;
};
export type PollMessage = {
  id?: number;
  roundNumber?: number;
  creationTime?: string;
  isOpen?: boolean;
  targetCharacter?: CharacterPrivateMessage;
  votes?: VoteMessage[];
};
export type EquipmentMessage = {
  id?: number;
  name?: string;
  level?: number;
};
export type BunkerMessage = {
  id?: number;
  square?: number;
  stayDays?: number;
  foodDays?: number;
  equipments?: EquipmentMessage[];
};
export type CataclysmMessage = {
  id?: number;
  description?: string;
};
export type RoomMessage = {
  id?: number;
  bunker?: BunkerMessage;
  cataclysm?: CataclysmMessage;
  joinCode?: string;
  isStarted?: boolean;
  isClosed?: boolean;
  characters?: CharacterPrivateMessage[];
};
export type BodyType = {
  id?: number;
  name?: string;
  level?: number;
};
export type Health = {
  id?: number;
  name?: string;
  level?: number;
};
export type Trait = {
  id?: number;
  name?: string;
  level?: number;
};
export type Hobby = {
  id?: number;
  name?: string;
  level?: number;
};
export type Profession = {
  id?: number;
  name?: string;
  level?: number;
};
export type Phobia = {
  id?: number;
  name?: string;
  level?: number;
};
export type Equipment = {
  id?: number;
  name?: string;
  level?: number;
};
export type Bag = {
  id?: number;
  name?: string;
  level?: number;
};
export type CharacterResponse = {
  id?: number;
  name?: string;
  age?: number;
  sex?:
    | "\u041C\u0443\u0436\u0447\u0438\u043D\u0430"
    | "\u0416\u0435\u043D\u0449\u0438\u043D\u0430";
  notes?: string;
  isActive?: boolean;
  user?: User;
  bodyType?: BodyType;
  health?: Health;
  trait?: Trait;
  hobby?: Hobby;
  profession?: Profession;
  phobia?: Phobia;
  equipment?: Equipment;
  bag?: Bag;
  openedFacts?: OpenedFacts;
};
export type CreateCharacterRequest = {
  name: string;
  notes: string;
  bodyTypeId: number;
  healthId: number;
  traitId: number;
  hobbyId: number;
  professionId: number;
  phobiaId: number;
  equipmentId: number;
  bagId: number;
  roomId: number;
};
export type OpenedFactsResponse = {
  bag?: string;
  bodyType?: string;
  equipment?: string;
  health?: string;
  hobby?: string;
  phobia?: string;
  profession?: string;
  trait?: string;
};
export type OpenedFactsRequest = {
  characterId: number;
};
export type AllFactsResponse = {
  name?: string;
  age?: string;
  sex?: string;
  bag?: string;
  bodyType?: string;
  equipment?: string;
  health?: string;
  hobby?: string;
  phobia?: string;
  profession?: string;
  trait?: string;
  notes?: string;
};
export type GenerateFactResponse = {
  bags?: Bag[];
  bodyTypes?: BodyType[];
  equipments?: Equipment[];
  healths?: Health[];
  hobbies?: Hobby[];
  phobiases?: Phobia[];
  professions?: Profession[];
  traits?: Trait[];
};
export const {
  useOpenFactMutation,
  useVoteMutation,
  useStartGameMutation,
  useCreatePoolMutation,
  useJoinRoomMutation,
  useCreateRoomMutation,
  useCreateCharacterMutation,
  useGetGameHistoryQuery,
  useGetRoomStateQuery,
  useGetPollsQuery,
  useGetApiMeQuery,
  useGetCharacterByIdQuery,
  useGetOpenedFactsQuery,
  useGetAllFactsQuery,
  useGenerateFactQuery,
  useGetAllRoomsQuery,
} = injectedRtkApi;
