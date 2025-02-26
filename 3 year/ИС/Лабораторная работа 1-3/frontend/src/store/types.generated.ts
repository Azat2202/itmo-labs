import { api } from "./baseApi";
const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    expelEverybody: build.mutation<
      ExpelEverybodyApiResponse,
      ExpelEverybodyApiArg
    >({
      query: (queryArg) => ({
        url: `/api/command/expel_everybody`,
        method: "PUT",
        params: { groupId: queryArg.groupId },
      }),
    }),
    getAllStudyGroups: build.query<
      GetAllStudyGroupsApiResponse,
      GetAllStudyGroupsApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup`,
        params: {
          page: queryArg.page,
          size: queryArg.size,
          sortBy: queryArg.sortBy,
          sortDirection: queryArg.sortDirection,
          groupName: queryArg.groupName,
          adminName: queryArg.adminName,
          semester: queryArg.semester,
          formOfEducation: queryArg.formOfEducation,
        },
      }),
    }),
    updateStudyGroup: build.mutation<
      UpdateStudyGroupApiResponse,
      UpdateStudyGroupApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup`,
        method: "PUT",
        body: queryArg.updateStudyGroupRequest,
      }),
    }),
    createStudyGroup: build.mutation<
      CreateStudyGroupApiResponse,
      CreateStudyGroupApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup`,
        method: "POST",
        body: queryArg.studyGroupRequest,
      }),
    }),
    approveAdminProposal: build.mutation<
      ApproveAdminProposalApiResponse,
      ApproveAdminProposalApiArg
    >({
      query: (queryArg) => ({
        url: `/api/admin/proposal/${queryArg.proposalId}`,
        method: "PUT",
      }),
    }),
    declineAdminProposal: build.mutation<
      DeclineAdminProposalApiResponse,
      DeclineAdminProposalApiArg
    >({
      query: (queryArg) => ({
        url: `/api/admin/proposal/${queryArg.proposalId}`,
        method: "DELETE",
      }),
    }),
    becomeAdmin: build.mutation<BecomeAdminApiResponse, BecomeAdminApiArg>({
      query: () => ({ url: `/api/user/proposal`, method: "POST" }),
    }),
    getFeedHistory: build.query<
      GetFeedHistoryApiResponse,
      GetFeedHistoryApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup/feed`,
        params: {
          page: queryArg.page,
          size: queryArg.size,
          sortBy: queryArg.sortBy,
          sortDirection: queryArg.sortDirection,
        },
      }),
    }),
    uploadFeed: build.mutation<UploadFeedApiResponse, UploadFeedApiArg>({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup/feed`,
        method: "POST",
        body: queryArg.body,
      }),
    }),
    getAllPersons: build.query<GetAllPersonsApiResponse, GetAllPersonsApiArg>({
      query: () => ({ url: `/api/collection/person` }),
    }),
    createPerson: build.mutation<CreatePersonApiResponse, CreatePersonApiArg>({
      query: (queryArg) => ({
        url: `/api/collection/person`,
        method: "POST",
        body: queryArg.personRequest,
      }),
    }),
    getAllLocations: build.query<
      GetAllLocationsApiResponse,
      GetAllLocationsApiArg
    >({
      query: () => ({ url: `/api/collection/location` }),
    }),
    createLocation: build.mutation<
      CreateLocationApiResponse,
      CreateLocationApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/location`,
        method: "POST",
        body: queryArg.locationRequest,
      }),
    }),
    getAllCoordinates: build.query<
      GetAllCoordinatesApiResponse,
      GetAllCoordinatesApiArg
    >({
      query: () => ({ url: `/api/collection/coordinates` }),
    }),
    createCoordinates: build.mutation<
      CreateCoordinatesApiResponse,
      CreateCoordinatesApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/coordinates`,
        method: "POST",
        body: queryArg.coordinatesRequest,
      }),
    }),
    register: build.mutation<RegisterApiResponse, RegisterApiArg>({
      query: (queryArg) => ({
        url: `/api/auth/register`,
        method: "POST",
        body: queryArg.registerUserDto,
      }),
    }),
    authenticate: build.mutation<AuthenticateApiResponse, AuthenticateApiArg>({
      query: (queryArg) => ({
        url: `/api/auth/login`,
        method: "POST",
        body: queryArg.jwtDto,
      }),
    }),
    me: build.query<MeApiResponse, MeApiArg>({
      query: () => ({ url: `/api/user/me` }),
    }),
    minGroupAdmin: build.query<MinGroupAdminApiResponse, MinGroupAdminApiArg>({
      query: () => ({ url: `/api/command/min_group_admin` }),
    }),
    countGroupAdmin: build.query<
      CountGroupAdminApiResponse,
      CountGroupAdminApiArg
    >({
      query: (queryArg) => ({
        url: `/api/command/count_group_admin`,
        params: { groupAdminId: queryArg.groupAdminId },
      }),
    }),
    getExpelledCount: build.query<
      GetExpelledCountApiResponse,
      GetExpelledCountApiArg
    >({
      query: () => ({ url: `/api/command/count_expelled` }),
    }),
    getStudyGroups: build.query<
      GetStudyGroupsApiResponse,
      GetStudyGroupsApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup/${queryArg.id}`,
      }),
    }),
    deleteStudyGroup: build.mutation<
      DeleteStudyGroupApiResponse,
      DeleteStudyGroupApiArg
    >({
      query: (queryArg) => ({
        url: `/api/collection/studyGroup/${queryArg.id}`,
        method: "DELETE",
      }),
    }),
    getAdminProposals: build.query<
      GetAdminProposalsApiResponse,
      GetAdminProposalsApiArg
    >({
      query: () => ({ url: `/api/admin/proposal` }),
    }),
    deleteByExpelled: build.mutation<
      DeleteByExpelledApiResponse,
      DeleteByExpelledApiArg
    >({
      query: (queryArg) => ({
        url: `/api/command/delete_by_expelled`,
        method: "DELETE",
        params: { expelled: queryArg.expelled },
      }),
    }),
  }),
  overrideExisting: false,
});
export { injectedRtkApi as studyGroupApi };
export type ExpelEverybodyApiResponse = /** status 200 OK */ StudyGroupResponse;
export type ExpelEverybodyApiArg = {
  groupId: number;
};
export type GetAllStudyGroupsApiResponse =
  /** status 200 OK */ PageStudyGroupResponse;
export type GetAllStudyGroupsApiArg = {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
  groupName?: string;
  adminName?: string;
  semester?: "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH";
  formOfEducation?:
    | "DISTANCE_EDUCATION"
    | "FULL_TIME_EDUCATION"
    | "EVENING_CLASSES";
};
export type UpdateStudyGroupApiResponse =
  /** status 200 OK */ StudyGroupResponse;
export type UpdateStudyGroupApiArg = {
  updateStudyGroupRequest: UpdateStudyGroupRequest;
};
export type CreateStudyGroupApiResponse =
  /** status 200 OK */ StudyGroupResponse;
export type CreateStudyGroupApiArg = {
  studyGroupRequest: StudyGroupRequest;
};
export type ApproveAdminProposalApiResponse = /** status 200 OK */ UserResponse;
export type ApproveAdminProposalApiArg = {
  proposalId: number;
};
export type DeclineAdminProposalApiResponse = unknown;
export type DeclineAdminProposalApiArg = {
  proposalId: number;
};
export type BecomeAdminApiResponse = /** status 200 OK */ AdminProposalResponse;
export type BecomeAdminApiArg = void;
export type GetFeedHistoryApiResponse = /** status 200 OK */ PageFeedResponse;
export type GetFeedHistoryApiArg = {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
};
export type UploadFeedApiResponse = unknown;
export type UploadFeedApiArg = {
  body: {
    file: Blob;
  };
};
export type GetAllPersonsApiResponse = /** status 200 OK */ PersonResponse[];
export type GetAllPersonsApiArg = void;
export type CreatePersonApiResponse = /** status 200 OK */ PersonResponse;
export type CreatePersonApiArg = {
  personRequest: PersonRequest;
};
export type GetAllLocationsApiResponse =
  /** status 200 OK */ LocationResponse[];
export type GetAllLocationsApiArg = void;
export type CreateLocationApiResponse = /** status 200 OK */ LocationResponse;
export type CreateLocationApiArg = {
  locationRequest: LocationRequest;
};
export type GetAllCoordinatesApiResponse =
  /** status 200 OK */ CoordinatesResponse[];
export type GetAllCoordinatesApiArg = void;
export type CreateCoordinatesApiResponse =
  /** status 200 OK */ CoordinatesResponse;
export type CreateCoordinatesApiArg = {
  coordinatesRequest: CoordinatesRequest;
};
export type RegisterApiResponse = /** status 200 OK */ UserResponse;
export type RegisterApiArg = {
  registerUserDto: RegisterUserDto;
};
export type AuthenticateApiResponse = /** status 200 OK */ JwtResponse;
export type AuthenticateApiArg = {
  jwtDto: JwtDto;
};
export type MeApiResponse = /** status 200 OK */ UserResponse;
export type MeApiArg = void;
export type MinGroupAdminApiResponse = /** status 200 OK */ StudyGroupResponse;
export type MinGroupAdminApiArg = void;
export type CountGroupAdminApiResponse = /** status 200 OK */ number;
export type CountGroupAdminApiArg = {
  groupAdminId?: number;
};
export type GetExpelledCountApiResponse = /** status 200 OK */ number;
export type GetExpelledCountApiArg = void;
export type GetStudyGroupsApiResponse = /** status 200 OK */ StudyGroupResponse;
export type GetStudyGroupsApiArg = {
  id: number;
};
export type DeleteStudyGroupApiResponse =
  /** status 200 OK */ StudyGroupResponse;
export type DeleteStudyGroupApiArg = {
  id: number;
};
export type GetAdminProposalsApiResponse =
  /** status 200 OK */ AdminProposalResponse[];
export type GetAdminProposalsApiArg = void;
export type DeleteByExpelledApiResponse =
  /** status 200 OK */ StudyGroupResponse;
export type DeleteByExpelledApiArg = {
  expelled: number;
};
export type Coordinates = {
  id?: number;
  x?: number;
  y?: number;
};
export type Location = {
  id?: number;
  x?: number;
  y?: number;
  z?: number;
  name: string;
};
export type Person = {
  id?: number;
  name?: string;
  eyeColor?: "RED" | "BLACK" | "BLUE" | "BROWN";
  hairColor?: "RED" | "BLACK" | "BLUE" | "BROWN";
  location?: Location;
  weight?: number;
  nationality?: "FRANCE" | "SPAIN" | "SOUTH_KOREA" | "JAPAN";
};
export type UserResponse = {
  username?: string;
  role?: "ADMIN" | "DEFAULT";
};
export type StudyGroupResponse = {
  id?: number;
  name?: string;
  coordinates?: Coordinates;
  creationDate?: string;
  studentsCount?: number;
  expelledStudents?: number;
  transferredStudents?: number;
  formOfEducation?:
    | "DISTANCE_EDUCATION"
    | "FULL_TIME_EDUCATION"
    | "EVENING_CLASSES";
  shouldBeExpelled?: number;
  semester?: "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH";
  groupAdmin?: Person;
  user?: UserResponse;
  isEditable?: boolean;
};
export type SortObject = {
  empty?: boolean;
  sorted?: boolean;
  unsorted?: boolean;
};
export type PageableObject = {
  offset?: number;
  sort?: SortObject;
  paged?: boolean;
  pageNumber?: number;
  pageSize?: number;
  unpaged?: boolean;
};
export type PageStudyGroupResponse = {
  totalPages?: number;
  totalElements?: number;
  first?: boolean;
  last?: boolean;
  size?: number;
  content?: StudyGroupResponse[];
  number?: number;
  sort?: SortObject;
  pageable?: PageableObject;
  numberOfElements?: number;
  empty?: boolean;
};
export type UpdateStudyGroupRequest = {
  id: number;
  name: string;
  coordinatesId: number;
  studentsCount: number;
  expelledStudents: number;
  transferredStudents: number;
  formOfEducation:
    | "DISTANCE_EDUCATION"
    | "FULL_TIME_EDUCATION"
    | "EVENING_CLASSES";
  shouldBeExpelled?: number;
  semester: "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH";
  groupAdminId?: number;
};
export type StudyGroupRequest = {
  name: string;
  coordinatesId: number;
  studentsCount: number;
  expelledStudents: number;
  transferredStudents: number;
  formOfEducation:
    | "DISTANCE_EDUCATION"
    | "FULL_TIME_EDUCATION"
    | "EVENING_CLASSES";
  shouldBeExpelled?: number;
  semester: "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH";
  groupAdminId?: number;
  isEditable?: boolean;
};
export type AdminProposalResponse = {
  id?: number;
  user?: UserResponse;
};
export type FeedResponse = {
  id?: number;
  creationDate?: string;
  feedUrl?: string;
  batchSize?: number;
  isSuccessful?: boolean;
  user?: UserResponse;
};
export type PageFeedResponse = {
  totalPages?: number;
  totalElements?: number;
  first?: boolean;
  last?: boolean;
  size?: number;
  content?: FeedResponse[];
  number?: number;
  sort?: SortObject;
  pageable?: PageableObject;
  numberOfElements?: number;
  empty?: boolean;
};
export type PersonResponse = {
  id?: number;
  name?: string;
  eyeColor?: "RED" | "BLACK" | "BLUE" | "BROWN";
  hairColor?: "RED" | "BLACK" | "BLUE" | "BROWN";
  location?: Location;
  weight?: number;
  nationality?: "FRANCE" | "SPAIN" | "SOUTH_KOREA" | "JAPAN";
};
export type PersonRequest = {
  name: string;
  eyeColor: "RED" | "BLACK" | "BLUE" | "BROWN";
  hairColor?: "RED" | "BLACK" | "BLUE" | "BROWN";
  locationId: number;
  weight: number;
  nationality?: "FRANCE" | "SPAIN" | "SOUTH_KOREA" | "JAPAN";
};
export type LocationResponse = {
  id?: number;
  x?: number;
  y?: number;
  z?: number;
  name?: string;
};
export type LocationRequest = {
  x: number;
  y: number;
  z: number;
  name: string;
};
export type CoordinatesResponse = {
  id?: number;
  x?: number;
  y?: number;
};
export type CoordinatesRequest = {
  x: number;
  y: number;
};
export type RegisterUserDto = {
  /** Юзернейм */
  username?: string;
  /** Пароль пользователя */
  password: string;
};
export type JwtResponse = {
  token?: string;
  expiresIn?: number;
};
export type JwtDto = {
  username: string;
  /** Пароль пользователя */
  password: string;
};
export const {
  useExpelEverybodyMutation,
  useGetAllStudyGroupsQuery,
  useUpdateStudyGroupMutation,
  useCreateStudyGroupMutation,
  useApproveAdminProposalMutation,
  useDeclineAdminProposalMutation,
  useBecomeAdminMutation,
  useGetFeedHistoryQuery,
  useUploadFeedMutation,
  useGetAllPersonsQuery,
  useCreatePersonMutation,
  useGetAllLocationsQuery,
  useCreateLocationMutation,
  useGetAllCoordinatesQuery,
  useCreateCoordinatesMutation,
  useRegisterMutation,
  useAuthenticateMutation,
  useMeQuery,
  useMinGroupAdminQuery,
  useCountGroupAdminQuery,
  useGetExpelledCountQuery,
  useGetStudyGroupsQuery,
  useDeleteStudyGroupMutation,
  useGetAdminProposalsQuery,
  useDeleteByExpelledMutation,
} = injectedRtkApi;
